package com.antigravity.sightwordslither.systems

data class Obstacle(val name: String, val requiredAction: String, val description: String)

class ObstacleManager {
    fun generateLevelObstacles(): List<Obstacle> {
        return listOf(
            Obstacle("Raging River", "SWIM", "A wide river blocks the path!"),
            Obstacle("Steep Cliff", "CLIMB", "A massive cliff face looms ahead!"),
            Obstacle("Thick Brambles", "CUT", "Thorny vines block the way!"),
            Obstacle("Fallen Tree", "JUMP", "A giant tree trunk is in the way!"),
            Obstacle("Muddy Swamp", "WADE", "Deep sticky mud covers the ground!")
        )
    }
    
    fun getAllActions(): List<String> {
        return listOf("SWIM", "CLIMB", "CUT", "JUMP", "WADE", "RUN", "HIDE")
    }
}
