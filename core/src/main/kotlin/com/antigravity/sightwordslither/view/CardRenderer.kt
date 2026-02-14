package com.antigravity.sightwordslither.view

import com.antigravity.sightwordslither.model.Card
import com.antigravity.sightwordslither.model.CardType
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle

class CardRenderer(private val backgroundTexture: Texture, private val font: BitmapFont) {
    private val layout = GlyphLayout()
    private val cardWidth = 150f
    private val cardHeight = 200f

    fun render(batch: SpriteBatch, card: Card, x: Float, y: Float) {
        // Draw card background
        batch.color = if (card.type == CardType.SIGHT_WORD) Color.LIGHT_GRAY else Color.ORANGE
        batch.draw(backgroundTexture, x, y, cardWidth, cardHeight)
        batch.color = Color.WHITE

        // Draw text centered
        layout.setText(font, card.text)
        val textX = x + (cardWidth - layout.width) / 2
        val textY = y + (cardHeight + layout.height) / 2
        font.draw(batch, layout, textX, textY)
    }

    fun getBounds(x: Float, y: Float): Rectangle {
        return Rectangle(x, y, cardWidth, cardHeight)
    }
}
