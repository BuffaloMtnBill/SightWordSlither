plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    val gdxVersion: String by project
    api("com.badlogicgames.gdx:gdx:$gdxVersion")
    api("org.jetbrains.kotlin:kotlin-stdlib")
}
