package com.antigravity.sightwordslither.model

import com.badlogic.gdx.math.MathUtils

class Deck {
    private val drawPile = ArrayList<Card>()
    private val discardPile = ArrayList<Card>()
    private val hand = ArrayList<Card>()

    fun addCard(card: Card) {
        drawPile.add(card)
    }

    fun shuffle() {
        drawPile.shuffle()
    }

    fun draw(amount: Int) {
        for (i in 0 until amount) {
            if (drawPile.isEmpty()) {
                if (discardPile.isEmpty()) break // No more cards
                recycleDiscard()
            }
            hand.add(drawPile.removeAt(0))
        }
    }

    fun playCard(card: Card) {
        if (hand.remove(card)) {
            discardPile.add(card)
        }
    }

    fun discardHand() {
        discardPile.addAll(hand)
        hand.clear()
    }

    private fun recycleDiscard() {
        drawPile.addAll(discardPile)
        discardPile.clear()
        shuffle()
    }

    fun getHand(): List<Card> = hand
    fun getDrawPileSize(): Int = drawPile.size
    fun getDiscardPileSize(): Int = discardPile.size
}
