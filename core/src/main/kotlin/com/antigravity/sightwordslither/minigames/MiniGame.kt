package com.antigravity.sightwordslither.minigames

import com.badlogic.gdx.graphics.g2d.SpriteBatch

interface MiniGame {
    fun start()
    fun render(batch: SpriteBatch, delta: Float)
    fun handleInput(x: Float, y: Float): Boolean // Returns true if input was handled
    fun isCompleted(): Boolean
    fun getResult(): Boolean // True if won/success
}
