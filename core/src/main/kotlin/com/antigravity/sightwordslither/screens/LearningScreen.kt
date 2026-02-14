package com.antigravity.sightwordslither.screens

import com.antigravity.sightwordslither.SightWordSlither
import com.antigravity.sightwordslither.model.LearningManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera

class LearningScreen(val game: SightWordSlither) : Screen {
    val camera = OrthographicCamera()

    private val learningManager = game.learningManager

    override fun show() {
        camera.setToOrtho(false, 1280f, 720f)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.5f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        game.font.draw(game.batch, "LEARNING MODE - Tap to Unlock Categories", 100f, 650f)
        
        for ((index, category) in learningManager.categories.withIndex()) {
            val y = 550f - index * 60f
            val status = if (learningManager.isCategoryUnlocked(category.name)) "[UNLOCKED] Animal: ${category.animalUnlock}" else "[LOCKED] (Tap to Learn)"
            game.font.draw(game.batch, "${category.name}: $status", 100f, y)
        }
        
        game.font.draw(game.batch, "Tap top to Return", 100f, 100f)
        game.batch.end()

        if (Gdx.input.justTouched()) {
             val touchY = 720f - Gdx.input.y.toFloat()
             
             // Simple unlock logic (click area)
             for (i in learningManager.categories.indices) {
                 val y = 550f - i * 60f
                 if (touchY > y - 20 && touchY < y + 20) {
                     val cat = learningManager.categories[i]
                     learningManager.unlockCategory(cat.name)
                 }
             }

             if (touchY < 150) {
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
