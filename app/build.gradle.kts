plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.shubham.newsappwithmvvm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.shubham.newsappwithmvvm"
        minSdk = 24
        targetSdk = 34
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
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.storage)

    implementation("androidx.compose.foundation:foundation:1.7.8")

    // Compose Navigation Dependency
    val nav_version = "2.8.5"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // dependency for extra material icons
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // Retrofit dependency
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Moshi converter for Retrofit
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    // Moshi for Kotlin
    implementation("com.squareup.moshi:moshi-kotlin:1.15.2")

    // For adding logging in Retrofit
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Landscapist using Coil Image Loading Library
    implementation("com.github.skydoves:landscapist-coil:2.4.7")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}