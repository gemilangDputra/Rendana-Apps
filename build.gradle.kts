// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // AGP 8.7.3 stabil
    id("com.android.application") version "8.9.1" apply false
    id("com.android.library") version "8.9.1" apply false 
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}