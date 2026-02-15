package com.antigravity.sightwordslither.screens

import com.antigravity.sightwordslither.SightWordSlither
import com.antigravity.sightwordslither.view.CardRenderer
import com.antigravity.sightwordslither.model.Card
import com.antigravity.sightwordslither.model.CardType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera

class AdventureScreen(val game: SightWordSlither) : Screen {
    val camera = OrthographicCamera()
    private val cardRenderer: CardRenderer
    
    // UI Properties
    private val cardY = 100f
    private val cardSpacing = 160f
    private var scrollX = 0f
    
    init {
         cardRenderer = CardRenderer(game.assetSystem.getCage(), game.font)
    }

    override fun show() {
        camera.setToOrtho(false, 1280f, 720f)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f) // Darker background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.projectionMatrix = camera.combined
        game.batch.begin()
        
        // Adventure Status (Top)
        game.font.setColor(Color.CYAN)
        game.font.draw(game.batch, "ADVENTURE MAP", 550f, 650f)
        game.font.setColor(Color.WHITE)
        game.font.draw(game.batch, "Select a Deck to start your journey:", 450f, 600f)
        
        // Render Decks as Cards
        val unlockedCategories = game.learningManager.categories.filter { 
            game.learningManager.isCategoryUnlocked(it.name) 
        }
        
        if (unlockedCategories.isEmpty()) {
             game.font.draw(game.batch, "No Decks Unlocked! Go to Learning Mode.", 400f, 360f)
        } else {
            for ((index, category) in unlockedCategories.withIndex()) {
                val x = 100f + index * cardSpacing - scrollX
                // Create a visual representation card
                val stringToRender = category.name.replace(" ", "\n") // Simple wrap
                val deckCard = Card("deck_$index", stringToRender, CardType.SIGHT_WORD)
                
                cardRenderer.render(game.batch, deckCard, x, cardY)
            }
        }
        
        // Back Button
        game.font.draw(game.batch, "[ BACK TO MENU ]", 100f, 680f)
        
        game.batch.end()
        
        handleInput(unlockedCategories)
    }
    
    private fun handleInput(categories: List<com.antigravity.sightwordslither.model.WordCategory>) {
        if (Gdx.input.justTouched()) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = 720f - Gdx.input.y.toFloat()
            
            // Back Button
            if (touchX < 300 && touchY > 650) {
                game.screen = MainMenuScreen(game)
                dispose()
                return
            }
            
            // Check Deck Clicks
             for ((index, category) in categories.withIndex()) {
                val x = 100f + index * cardSpacing - scrollX
                if (cardRenderer.getBounds(x, cardY).contains(touchX, touchY)) {
                    // Launch Game with this category
                    println("Selected Deck: ${category.name}")
                    game.screen = GameScreen(game, category)
                    dispose()
                    return
                }
            }
        }
        
        // Simple scroll (placeholders for now) (drag implementation omitted for brevity)
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}
