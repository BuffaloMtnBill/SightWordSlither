plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    val gdxVersion: String by project
    api("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    api("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
}

application {
    mainClass.set("com.antigravity.sightwordslither.desktop.DesktopLauncherKt")
}

// Ensure resources are copied
sourceSets.main.configure {
    resources.srcDirs("${project.rootDir}/assets")
}
