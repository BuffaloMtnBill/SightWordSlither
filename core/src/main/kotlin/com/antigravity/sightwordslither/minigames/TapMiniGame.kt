package com.antigravity.sightwordslither.minigames

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle

class TapMiniGame(val font: BitmapFont) : MiniGame {
    private var completed = false
    private var won = false
    private val target = Rectangle(400f, 300f, 200f, 200f)
    private var timer = 5f

    override fun start() {
        completed = false
        won = false
        timer = 5f
        // Random position
        target.x = MathUtils.random(200f, 1000f)
        target.y = MathUtils.random(200f, 500f)
    }

    override fun render(batch: SpriteBatch, delta: Float) {
        timer -= delta
        if (timer <= 0) {
            completed = true
            won = false
        }

        font.setColor(Color.YELLOW)
        font.draw(batch, "TAP THE BOX! Time: ${timer.toInt()}", 500f, 650f)
        
        // Draw simple rect (placeholder visual)
        font.draw(batch, "[ TARGET ]", target.x + 50, target.y + 100)
        font.setColor(Color.WHITE)
    }

    override fun handleInput(x: Float, y: Float): Boolean {
        if (completed) return false
        
        if (target.contains(x, y)) {
            completed = true
            won = true
            return true
        }
        return false
    }

    override fun isCompleted() = completed
    override fun getResult() = won
}
