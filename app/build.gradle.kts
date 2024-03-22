plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.siwika"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.siwika"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        mlModelBinding = true
    }
}
dependencies {
    //region Android X Library
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    //endregion
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")
    //region Android X Jetpack Compose
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation(platform("androidx.compose:compose-bom:2024.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.4")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    //endregion
    //region CameraX Library
    implementation("androidx.camera:camera-camera2:1.3.2")
    //implementation("androidx.camera:camera-core:1.3.2")
    //implementation("androidx.camera:camera-extensions:1.3.2")
    implementation("androidx.camera:camera-lifecycle:1.3.2")
    //implementation("androidx.camera:camera-video:1.3.2")
    implementation("androidx.camera:camera-view:1.3.2")
    //endregion
    //region Text Recognition
    //implementation("com.google.mlkit:vision-common:17.3.0")
    //implementation("com.google.android.gms:play-services-mlkit-text-recognition-common:19.0.0")
    //implementation("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
    //implementation("androidx.compose.material3:material3-android:1.2.1")
    //endregion
    //implementation("com.google.android.gms:play-services-tflite-acceleration-service:16.0.0-beta01")
    //region Tensor Flow
    //implementation("org.tensorflow:tensorflow-lite:2.4.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.1.0")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.3.0")
    //endregion
    //region Android Unit Test and U.I. Test Library
    //testImplementation("junit:junit:4.14-SNAPSHOT")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    //endregion
}