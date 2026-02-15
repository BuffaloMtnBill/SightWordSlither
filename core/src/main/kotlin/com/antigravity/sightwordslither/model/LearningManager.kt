package com.antigravity.sightwordslither.model

import com.badlogic.gdx.Gdx

data class WordCategory(
    val name: String, 
    val tiers: Map<Int, List<String>>, 
    val verbs: List<String>,
    val animalUnlock: String
) {
    val allWords: List<String> get() = tiers.values.flatten()
}

class LearningManager {
    val categories = mutableListOf<WordCategory>()
    
    private val unlockedCategories = HashSet<String>()
    private val unlockedTiers = mutableMapOf<String, HashSet<Int>>() // Category -> Set of Tiers
    private val unlockedVerbs = mutableMapOf<String, HashSet<String>>() // Category -> Set of Verbs
    private val learnedWords = HashSet<String>()
    
    init {
        loadCategories()
        // Default unlock
        unlockCategory("Easiest Words")
        unlockTier("Easiest Words", 1)
        unlockVerb("Easiest Words", "walk")
    }
    
    private fun loadCategories() {
        try {
            val file = Gdx.files.internal("words.txt")
            if (!file.exists()) {
                println("words.txt not found!")
                return
            }
            
            val text = file.readString()
            val sections = text.split("\n\n")
            
            for (section in sections) {
                val lines = section.lines().filter { it.isNotBlank() }
                if (lines.isEmpty()) continue
                
                val categoryName = lines[0].trim()
                val tiers = mutableMapOf<Int, List<String>>()
                val verbs = mutableListOf<String>()
                
                for (i in 1 until lines.size) {
                    val line = lines[i].trim()
                    when {
                        line.startsWith("Tier") -> {
                            val parts = line.split(":")
                            val tierNum = parts[0].replace("Tier", "").trim().toInt()
                            val words = parts[1].split(",").map { it.trim().uppercase() }
                            tiers[tierNum] = words
                        }
                        line.startsWith("Verbs") -> {
                            val verbList = line.split(":")[1].split(",").map { it.trim().uppercase() }
                            verbs.addAll(verbList)
                        }
                    }
                }
                
                categories.add(WordCategory(categoryName, tiers, verbs, "Animal for $categoryName"))
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback
            categories.add(WordCategory("Fallback", mapOf(1 to listOf("CAT", "DOG")), listOf("WALK"), "Cat"))
        }
    }

    fun unlockCategory(categoryName: String) {
        unlockedCategories.add(categoryName)
    }

    fun isCategoryUnlocked(categoryName: String) = unlockedCategories.contains(categoryName)

    fun unlockTier(categoryName: String, tier: Int) {
        unlockedTiers.getOrPut(categoryName) { HashSet() }.add(tier)
    }

    fun isTierUnlocked(categoryName: String, tier: Int): Boolean {
        return unlockedTiers[categoryName]?.contains(tier) ?: false
    }

    fun unlockVerb(categoryName: String, verb: String) {
        unlockedVerbs.getOrPut(categoryName) { HashSet() }.add(verb.uppercase())
    }

    fun isVerbUnlocked(categoryName: String, verb: String): Boolean {
        return unlockedVerbs[categoryName]?.contains(verb.uppercase()) ?: false
    }
    
    fun getUnlockedVerbs(categoryName: String): List<String> {
        return unlockedVerbs[categoryName]?.toList() ?: emptyList()
    }

    fun getUnlockedTierWords(categoryName: String): List<String> {
        val category = categories.find { it.name == categoryName } ?: return emptyList()
        val unlocked = unlockedTiers[categoryName] ?: return emptyList()
        return category.tiers.filterKeys { unlocked.contains(it) }.values.flatten()
    }
    
    fun learnWord(word: String) {
        learnedWords.add(word)
    }
    
    fun getUnlockedAnimals(): List<String> {
        return categories.filter { unlockedCategories.contains(it.name) }.map { it.animalUnlock }
    }
}
