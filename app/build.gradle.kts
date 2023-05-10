plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {

    compileSdk = 33
    defaultConfig {
        minSdk = 26
        targetSdk = 33
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
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("com.google.dagger:dagger:2.45")
    kapt("com.google.dagger:dagger-compiler:2.45")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.core:core-ktx:1.10.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.9")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.room:room-runtime:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")
    annotationProcessor("androidx.room:room-compiler:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")
}
