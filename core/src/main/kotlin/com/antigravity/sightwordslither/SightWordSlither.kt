package com.antigravity.sightwordslither

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.antigravity.sightwordslither.screens.MainMenuScreen
import com.antigravity.sightwordslither.systems.AssetSystem
import com.antigravity.sightwordslither.systems.SoundManager
import com.antigravity.sightwordslither.model.LearningManager

/**
 * The main game class for Sight Word Slither.
 *
 * This class extends [Game] to manage the game lifecycle and screen transitions.
 * It also holds global references to key systems like [AssetSystem], [SoundManager],
 * and the [SpriteBatch] used for rendering.
 */
class SightWordSlither : Game() {
    lateinit var batch: SpriteBatch
    lateinit var font: BitmapFont
    lateinit var assetSystem: AssetSystem
    val learningManager = LearningManager()
    lateinit var soundManager: SoundManager

    override fun create() {
        batch = SpriteBatch()
        font = BitmapFont()
        assetSystem = AssetSystem()
        assetSystem.loadAll()
        assetSystem.finishLoading()
        
        soundManager = SoundManager()
        
        setScreen(MainMenuScreen(this))
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        assetSystem.dispose()
        screen?.dispose()
        super.dispose()
    }
}
