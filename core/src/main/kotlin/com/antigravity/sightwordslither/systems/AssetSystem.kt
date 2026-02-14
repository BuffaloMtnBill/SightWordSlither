package com.antigravity.sightwordslither.systems

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Disposable

/**
 * Manages the loading and retrieval of game assets.
 *
 * This system handles texture loading via LibGDX's AssetManager and provides
 * type-safe access to commonly used assets. It implements [Disposable] to ensure
 * proper resource cleanup.
 */
class AssetSystem : Disposable {
    val manager = AssetManager()

    /**
     * Queues all game assets for loading.
     *
     * This method adds all required textures to the AssetManager's loading queue.
     * Call [finishLoading] to process the queue synchronously.
     */
    fun loadAll() {
        manager.load("background.png", Texture::class.java)
        manager.load("animal_cage.png", Texture::class.java)
        manager.load("baby_animal.png", Texture::class.java)
    }

    /**
     * Blocks until all queued assets have finished loading.
     */
    fun finishLoading() {
        manager.finishLoading()
    }

    /** Returns the background texture. */
    fun getBackground(): Texture = manager.get("background.png", Texture::class.java)

    /** Returns the animal cage texture. */
    fun getCage(): Texture = manager.get("animal_cage.png", Texture::class.java)

    /** Returns the baby animal texture. */
    fun getBabyAnimal(): Texture = manager.get("baby_animal.png", Texture::class.java)

    /**
     * Disposes of the AssetManager and all loaded resources.
     */
    override fun dispose() {
        manager.dispose()
    }
}
