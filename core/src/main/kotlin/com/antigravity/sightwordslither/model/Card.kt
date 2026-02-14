package com.antigravity.sightwordslither.model

enum class CardType {
    SIGHT_WORD,
    ACTION, // Move, attack, etc.
    ITEM
}

data class Card(
    val id: String,
    val text: String,
    val type: CardType,
    val audioPath: String? = null,
    val imagePath: String? = null,
    val energyCost: Int = 1
)
