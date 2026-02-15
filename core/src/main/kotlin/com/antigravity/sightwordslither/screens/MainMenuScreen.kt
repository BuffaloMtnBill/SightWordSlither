package com.antigravity.sightwordslither.screens

import com.antigravity.sightwordslither.SightWordSlither
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3

class MainMenuScreen(val game: SightWordSlither) : Screen {
    val camera = OrthographicCamera()
    val touchPoint = Vector3()

    override fun show() {
        camera.setToOrtho(false, 1280f, 720f)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        game.batch.draw(game.assetSystem.getBackground(), 0f, 0f, 1280f, 720f)
        game.font.setColor(Color.WHITE)
        game.font.draw(game.batch, "Sight Word Search Squad", 100f, 650f)
        game.font.draw(game.batch, "Tap here for ADVENTURE MODE", 100f, 550f)
        game.font.draw(game.batch, "Tap here for LEARNING MODE", 100f, 500f)
        
        // Display Rescue Status
        game.font.draw(game.batch, "RESCUED ANIMALS:", 100f, 400f)
        val unlocked = game.learningManager.getUnlockedAnimals()
        if (unlocked.isEmpty()) {
            game.font.draw(game.batch, "(None yet! Go to Learning Mode)", 100f, 350f)
        } else {
            for ((index, animal) in unlocked.withIndex()) {
                game.font.draw(game.batch, "- $animal", 100f, 350f - index * 30f)
            }
        }
        
        game.batch.end()

        if (Gdx.input.justTouched()) {
            val touchY = 720f - Gdx.input.y.toFloat()
            // Adventure Mode (approx y: 550)
            if (touchY > 530 && touchY < 570) {
                game.screen = AdventureScreen(game)
                dispose()
            } 
            // Learning Mode (approx y: 500)
            else if (touchY > 480 && touchY < 520) {
                game.screen = LearningScreen(game)
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
