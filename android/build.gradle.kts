plugins {
    id("com.android.application")
    kotlin("android")
}

repositories {
    google()
    mavenCentral()
}

android {
    namespace = "com.antigravity.sightwordslither"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.antigravity.sightwordslither"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    sourceSets {
        getByName("main") {
            // assets folder shared with desktop
            assets.srcDirs("${project.rootDir}/assets")
            java.srcDirs("src/main/java", "src/main/kotlin")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":core"))
    val gdxVersion: String by project
    implementation("com.badlogicgames.gdx:gdx-backend-android:$gdxVersion")
    // Use natives-armeabi-v7a, natives-arm64-v8a, natives-x86, natives-x86_64
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")
}
