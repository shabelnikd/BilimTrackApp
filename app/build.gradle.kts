plugins {
    // Standard plugins
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Plugin for serialization
    kotlin("plugin.serialization") version "2.1.10"
    // Safeargs plugin
    id("androidx.navigation.safeargs.kotlin")

    // Add @Parcelize usually for safe arguments
    id("kotlin-parcelize")

    // Ksp plugin
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.shabelnikd.bilimtrack"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shabelnikd.bilimtrack"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    // Enable View Binding on app
    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    // See libs.versions.toml for descriptions about libraries


    // <------------ Often Used ---------->
    //Live Data & View Model
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    // Koin
    implementation(libs.koin.android.v402)

    // Fragments
    implementation(libs.androidx.fragment.ktx)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // <------------ End ------------>


    // Standard Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Legacy??
    implementation(libs.androidx.legacy.support.v4)

    //For Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Dots Indicator
    implementation(libs.dotsindicator)
    //Lottie
    implementation(libs.lottie)
    //Circle Profile Image
    implementation(libs.circleimageview)
    // Glide
    implementation(libs.glide)

    // Webkit
    implementation(libs.androidx.webkit)

}