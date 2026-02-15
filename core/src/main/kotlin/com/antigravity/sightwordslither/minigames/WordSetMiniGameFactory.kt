package com.antigravity.sightwordslither.minigames

import com.antigravity.sightwordslither.model.WordCategory
import com.badlogic.gdx.graphics.g2d.BitmapFont

object WordSetMiniGameFactory {
    fun createMiniGame(category: WordCategory, font: BitmapFont): MiniGame {
        // In the future, we can switch based on category.name
        // For now, "Easiest Words" -> TapMiniGame
        // Others -> TapMiniGame (Default)
        
        return when (category.name) {
            "Easiest Words" -> TapMiniGame(font)
            else -> TapMiniGame(font)
        }
    }
}
