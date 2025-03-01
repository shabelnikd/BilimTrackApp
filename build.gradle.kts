// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Top level plugins
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false

    // Top level Ksp
    alias(libs.plugins.devtools.ksp) apply false
}

buildscript {
    repositories {
        google()
    }
    dependencies {
        // For safeargs
        classpath(libs.androidx.navigation.safe.args.gradle.plugin)
        // Kotlin gradle plugin
        classpath(libs.kotlin.gradle.plugin)
    }
}
