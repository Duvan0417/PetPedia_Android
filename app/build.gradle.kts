plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.primerproyecto"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.primerproyecto"
        minSdk = 24
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Usa solo una de las siguientes dos líneas para cada dependencia
    implementation(libs.androidx.core.ktx) // O
    // implementation 'androidx.core:core-ktx:1.12.0'

    implementation(libs.androidx.lifecycle.runtime.ktx) // O
    // implementation 'androidx.lifecycle:lifecycle-runtime-ktx:2.7.0'

    implementation(libs.androidx.activity.compose) // O
    // implementation 'androidx.activity:activity-compose:1.8.2'

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui) // O
    // implementation 'androidx.compose.ui:ui'

    implementation(libs.androidx.ui.graphics) // O
    // implementation 'androidx.compose.ui:ui-tooling-preview'

    implementation(libs.androidx.ui.tooling.preview) // O
    // implementation 'androidx.compose.material3:material3'

    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Dependencias de prueba
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.core:core-splashscreen:1.0.1")
}
