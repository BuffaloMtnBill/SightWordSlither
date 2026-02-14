package com.antigravity.sightwordslither.systems

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.files.FileHandle

/**
 * Handles the playback of sound effects in the game.
 *
 * This manager provides a simple interface for playing sounds by name.
 * It loads sound files from the "audio/" directory.
 */
class SoundManager {
    
    /**
     * Plays a sound effect by its file name (without extension).
     *
     * Attempts to load and play an audio file located at "audio/$name.mp3".
     * If the file does not exist, an error message is printed to stdout.
     *
     * @param name The name of the sound file to play (excluding .mp3 extension).
     */
    fun playSound(name: String) {
        val file: FileHandle = Gdx.files.internal("audio/$name.mp3")
        if (file.exists()) {
            val sound = Gdx.audio.newSound(file)
            sound.play()
            // Note: in a real game we'd manage disposal more carefully
        } else {
            println("Sound not found: $name")
        }
    }
}
