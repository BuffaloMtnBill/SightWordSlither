package com.antigravity.sightwordslither.model

data class WordCategory(val name: String, val words: List<String>, val animalUnlock: String)

class LearningManager {
    val categories = listOf(
        WordCategory("Colors", listOf("RED", "BLUE", "GREEN"), "Tiger Cub"),
        WordCategory("Numbers", listOf("ONE", "TWO", "THREE"), "Panda"),
        WordCategory("Actions", listOf("RUN", "JUMP", "PLAY"), "Monkey")
    )
    
    private val unlockedCategories = HashSet<String>()
    private val learnedWords = HashSet<String>()

    fun unlockCategory(categoryName: String) {
        unlockedCategories.add(categoryName)
    }

    fun isCategoryUnlocked(categoryName: String) = unlockedCategories.contains(categoryName)
    
    fun learnWord(word: String) {
        learnedWords.add(word)
    }
    
    fun getUnlockedAnimals(): List<String> {
        return categories.filter { unlockedCategories.contains(it.name) }.map { it.animalUnlock }
    }
}
