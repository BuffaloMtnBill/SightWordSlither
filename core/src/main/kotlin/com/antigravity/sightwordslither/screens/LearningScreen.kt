package com.antigravity.sightwordslither.screens

import com.antigravity.sightwordslither.SightWordSlither
import com.antigravity.sightwordslither.model.LearningManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
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
        game.font.draw(game.batch, "LEARNING MODE - Tap to Unlock Tiers & Verbs", 100f, 680f)
        
        var currentY = 620f
        for (category in learningManager.categories) {
            game.font.setColor(Color.CYAN)
            game.font.draw(game.batch, "${category.name}:", 100f, currentY)
            game.font.setColor(Color.WHITE)
            currentY -= 30f
            
            // Tiers
            for (tier in category.tiers.keys.sorted()) {
                val status = if (learningManager.isTierUnlocked(category.name, tier)) "[UNLOCKED]" else "[LOCKED]"
                game.font.draw(game.batch, "  Tier $tier: $status", 120f, currentY)
                currentY -= 25f
            }
            
            // Verbs
            for (verb in category.verbs) {
                val status = if (learningManager.isVerbUnlocked(category.name, verb)) "[UNLOCKED]" else "[LOCKED]"
                game.font.draw(game.batch, "  Verb $verb: $status", 120f, currentY)
                currentY -= 25f
            }
            
            currentY -= 20f // Extra spacing between categories
            if (currentY < 50f) break // Simple clip
        }
        
        game.font.draw(game.batch, "Tap top-right to Return", 1000f, 680f)
        game.batch.end()

        if (Gdx.input.justTouched()) {
             val touchX = Gdx.input.x.toFloat()
             val touchY = 720f - Gdx.input.y.toFloat()
             
             // Unlock logic
             var clickY = 620f
             for (category in learningManager.categories) {
                 clickY -= 30f // Skip category title
                 
                 // Check Tiers
                 for (tier in category.tiers.keys.sorted()) {
                     if (touchY > clickY - 12 && touchY < clickY + 12 && touchX < 500) {
                         learningManager.unlockCategory(category.name)
                         learningManager.unlockTier(category.name, tier)
                     }
                     clickY -= 25f
                 }
                 
                 // Check Verbs
                 for (verb in category.verbs) {
                     if (touchY > clickY - 12 && touchY < clickY + 12 && touchX < 500) {
                         learningManager.unlockCategory(category.name)
                         learningManager.unlockVerb(category.name, verb)
                     }
                     clickY -= 25f
                 }
                 
                 clickY -= 20f
             }

             if (touchX > 900 && touchY > 600) {
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
