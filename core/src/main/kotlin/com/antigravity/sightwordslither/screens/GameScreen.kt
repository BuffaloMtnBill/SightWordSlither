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

class GameScreen(val game: SightWordSlither) : Screen {
    val camera = OrthographicCamera()
    
    private val deck = Deck()
    private lateinit var cardRenderer: CardRenderer
    private val handY = 50f
    private val handSpacing = 160f

    init {
        // Initialize simple deck for testing
        deck.addCard(Card("1", "THE", CardType.SIGHT_WORD))
        deck.addCard(Card("2", "AND", CardType.SIGHT_WORD))
        deck.addCard(Card("3", "YOU", CardType.SIGHT_WORD))
        deck.addCard(Card("4", "RESCUE", CardType.ACTION))
        deck.shuffle()
        deck.draw(3)
    }


    private var activeMiniGame: MiniGame? = null
    private var paused = false

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
                    game.font.draw(game.batch, "MINI GAME WON! ANIMAL SAVED!", 400f, 400f)
                } else {
                    game.font.draw(game.batch, "MINI GAME LOST!", 400f, 400f)
                }
                // Click to dismiss result
                if (Gdx.input.justTouched()) {
                    activeMiniGame = null
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
        
        // Draw Hand
        val hand = deck.getHand()
        for ((index, card) in hand.withIndex()) {
            val x = 100f + index * handSpacing
            cardRenderer.render(game.batch, card, x, handY)
        }

        game.font.draw(game.batch, "RESCUE MODE - Deck: ${deck.getDrawPileSize()} Discard: ${deck.getDiscardPileSize()}", 100f, 600f)
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
                    println("Played card: ${card.text}")
                    deck.playCard(card)
                    
                    // Trigger MiniGame on specific cards (or all for test)
                    if (card.text == "RESCUE") {
                        activeMiniGame = TapMiniGame(game.font)
                        activeMiniGame!!.start()
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
