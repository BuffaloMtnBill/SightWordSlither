package com.antigravity.sightwordslither.screens

import com.antigravity.sightwordslither.SightWordSlither
import com.antigravity.sightwordslither.minigames.MiniGame
import com.antigravity.sightwordslither.minigames.TapMiniGame
import com.antigravity.sightwordslither.model.Card
import com.antigravity.sightwordslither.model.CardType
import com.antigravity.sightwordslither.model.Deck
import com.antigravity.sightwordslither.view.CardRenderer
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera

import com.antigravity.sightwordslither.model.WordCategory

import com.antigravity.sightwordslither.systems.ObstacleManager
import com.antigravity.sightwordslither.systems.Obstacle

class GameScreen(val game: SightWordSlither, val targetCategory: WordCategory? = null) : Screen {
    val camera = OrthographicCamera()
    
    private val deck = Deck()
    private lateinit var cardRenderer: CardRenderer
    private val handY = 50f
    private val handSpacing = 160f
    
    // Obstacle Logic
    private val obstacleManager = ObstacleManager()
    private val obstacles = obstacleManager.generateLevelObstacles()
    private var currentObstacleIndex = 0
    private var levelComplete = false
    
    private var activeMiniGame: MiniGame? = null
    private var paused = false

    init {
        initializeDeck()
    }
    
    private fun initializeDeck() {
        val learningManager = game.learningManager
        if (targetCategory != null) {
             // Load unlocked sight words from this category
             val words = learningManager.getUnlockedTierWords(targetCategory.name)
             for (word in words) {
                deck.addCard(Card("word_${word}", word, CardType.SIGHT_WORD))
            }
            
            // Load unlocked verbs as action cards
            val verbs = learningManager.getUnlockedVerbs(targetCategory.name)
            for (verb in verbs) {
                deck.addCard(Card("action_${verb}_1", verb, CardType.ACTION))
                deck.addCard(Card("action_${verb}_2", verb, CardType.ACTION))
            }
        } else {
             // Fallback: Load all unlocked from all categories
            for (category in learningManager.categories) {
                if (learningManager.isCategoryUnlocked(category.name)) {
                    val words = learningManager.getUnlockedTierWords(category.name)
                    for (word in words) {
                        deck.addCard(Card("word_${word}", word, CardType.SIGHT_WORD))
                    }
                    val verbs = learningManager.getUnlockedVerbs(category.name)
                    for (verb in verbs) {
                        deck.addCard(Card("action_${verb}_1", verb, CardType.ACTION))
                        deck.addCard(Card("action_${verb}_2", verb, CardType.ACTION))
                    }
                }
            }
        }
        
        // Ensure we always have some basic fallback if deck is too small
        if (deck.getDrawPileSize() < 5) {
            deck.addCard(Card("fallback_1", "I", CardType.SIGHT_WORD))
            deck.addCard(Card("fallback_2", "YOU", CardType.SIGHT_WORD))
            deck.addCard(Card("fallback_3", "THE", CardType.SIGHT_WORD))
        }

        deck.shuffle()
        deck.draw(3)
    }
    
    private fun getCurrentObstacle(): Obstacle? {
        if (currentObstacleIndex < obstacles.size) {
            return obstacles[currentObstacleIndex]
        }
        return null
    }

    override fun show() {
        camera.setToOrtho(false, 1280f, 720f)
        cardRenderer = CardRenderer(game.assetSystem.getCage(), game.font) // Using cage as card bg placeholder
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.2f, 0.5f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.projectionMatrix = camera.combined
        game.batch.begin()
        
        if (paused) {
            game.font.setColor(Color.YELLOW)
            game.font.draw(game.batch, "GAME PAUSED", 550f, 400f)
            game.font.draw(game.batch, "Tap to RESUME", 550f, 350f)
            game.font.draw(game.batch, "Tap Top-Right to MENU", 1000f, 700f)
            game.font.setColor(Color.WHITE)
        } else if (activeMiniGame != null) {
            activeMiniGame!!.render(game.batch, delta)
            if (activeMiniGame!!.isCompleted()) {
                if (activeMiniGame!!.getResult()) {
                    game.font.draw(game.batch, "OBSTACLE CLEARED!", 500f, 400f)
                    // Mark progress
                    if (Gdx.input.justTouched()) {
                        currentObstacleIndex++
                        activeMiniGame = null
                        if (currentObstacleIndex >= obstacles.size) {
                            levelComplete = true
                        }
                    }
                } else {
                    game.font.draw(game.batch, "TRY AGAIN!", 550f, 400f)
                    if (Gdx.input.justTouched()) {
                        activeMiniGame = null
                    }
                }
            }
        } else {
            renderGameLoop(delta)
            // Draw Pause Button
            game.font.draw(game.batch, "[PAUSE]", 1150f, 700f)
        }
        
        game.batch.end()
        
        if (paused) {
            handlePauseInput()
        } else if (activeMiniGame != null && !activeMiniGame!!.isCompleted()) {
             if (Gdx.input.justTouched()) {
                 val touchX = Gdx.input.x.toFloat()
                 val touchY = 720f - Gdx.input.y.toFloat()
                 activeMiniGame!!.handleInput(touchX, touchY)
             }
        } else if (activeMiniGame == null) {
            handleGameInput()
        }
    }
    
    private fun handlePauseInput() {
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = 720f - Gdx.input.y.toFloat()
            
            // Resume (Center area approximation)
            if (touchX > 400 && touchX < 800 && touchY > 300 && touchY < 450) {
                paused = false
            }
            // Return to Menu (Top Right)
            if (touchX > 1000 && touchY > 600) {
                game.screen = MainMenuScreen(game)
                dispose()
            }
        }
    }
    
    private fun renderGameLoop(delta: Float) {
        game.batch.draw(game.assetSystem.getBackground(), 0f, 0f, 1280f, 720f)
        
        // Draw Obstacle Info
        val obstacle = getCurrentObstacle()
        if (obstacle != null) {
            game.font.setColor(Color.RED)
            game.font.draw(game.batch, "OBSTACLE: ${obstacle.name}", 100f, 680f)
            game.font.setColor(Color.WHITE)
            game.font.draw(game.batch, "${obstacle.description}", 100f, 640f)
            game.font.draw(game.batch, "Required Action: ${obstacle.requiredAction}", 100f, 600f)
        } else {
             game.font.draw(game.batch, "ALL OBSTACLES CLEARED!", 100f, 650f)
        }
        
        // Draw Hand
        val hand = deck.getHand()
        for ((index, card) in hand.withIndex()) {
            val x = 100f + index * handSpacing
            cardRenderer.render(game.batch, card, x, handY)
        }

        game.font.draw(game.batch, "Progress: $currentObstacleIndex / 5", 900f, 680f)
        game.font.draw(game.batch, "Deck: ${deck.getDrawPileSize()}", 900f, 640f)
    }

    private fun handleGameInput() {
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = 720f - Gdx.input.y.toFloat()
            
            // Trigger Pause (Top Right)
            if (touchX > 1100 && touchY > 650) {
                paused = true
                return
            }
            
            val hand = deck.getHand()
            for (i in hand.indices.reversed()) {
                val cardX = 100f + i * handSpacing
                if (cardRenderer.getBounds(cardX, handY).contains(touchX, touchY)) {
                    val card = hand[i]
                    deck.playCard(card)
                    
                    val obstacle = getCurrentObstacle()
                    
                    if (card.type == CardType.ACTION) {
                        if (obstacle != null && card.text == obstacle.requiredAction) {
                             // Correct Action! Trigger MiniGame
                             println("Correct Action: ${card.text}")
                             val category = targetCategory ?: game.learningManager.categories.firstOrNull() ?: com.antigravity.sightwordslither.model.WordCategory("Default", emptyMap(), emptyList(), "Cat")
                             activeMiniGame = com.antigravity.sightwordslither.minigames.WordSetMiniGameFactory.createMiniGame(category, game.font)
                             activeMiniGame!!.start()
                        } else {
                            // Wrong Action
                            println("Wrong Action! Needed ${obstacle?.requiredAction}, played ${card.text}")
                             // Ideally show feedback "Try Again!"
                        }
                    } else {
                         // Sight Word - Just played for practice/cycling
                         // Maybe add concentration logic later
                    }
                    
                    deck.draw(1) 
                    break 
                }
            }
             
             if (touchY > 600) {
                 game.screen = MainMenuScreen(game)
                 dispose()
             }
        }
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}
