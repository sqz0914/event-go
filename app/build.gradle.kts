import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.eventgo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.eventgo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Load properties from local.properties
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        buildConfigField("String", "MAPS_API_KEY", "\"${localProperties["MAPS_API_KEY"]}\"")
        buildConfigField("String", "TICKETMASTER_API_KEY", "\"${localProperties["TICKETMASTER_API_KEY"]}\"")
        buildConfigField("String", "OTHER_API_KEY", "\"${localProperties["OTHER_API_KEY"]}\"")
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

    buildFeatures {
        viewBinding = true
        buildConfig = true
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
    // Dependencies to use Room as the local database
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:$room_version")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$room_version")

    // RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$room_version")

    // Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$room_version")

    // Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")

    // Dependencies to use Retrofit for API calls
    implementation("com.google.code.gson:gson:2.11.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Dependencies to include Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Dependencies to get and geohash locations
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("ch.hsr:geohash:1.4.0")

    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}