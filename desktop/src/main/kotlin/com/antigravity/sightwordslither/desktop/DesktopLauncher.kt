package com.antigravity.sightwordslither.desktop

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.antigravity.sightwordslither.SightWordSlither

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setWindowedMode(1280, 720)
    config.setTitle("Sight Word Slither: Baby Animal Rescue")
    config.setForegroundFPS(60)
    Lwjgl3Application(SightWordSlither(), config)
}
