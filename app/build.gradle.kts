plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.assignment_mobile_app"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.assignment_mobile_app"
        minSdk = 24
        targetSdk = 36
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

    packaging {
        resources {
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.recyclerview)
    implementation("org.osmdroid:osmdroid-android:6.1.20")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("com.google.android.gms:play-services-auth:21.4.0")
    implementation("com.google.apis:google-api-services-drive:v3-rev20261106-2.9.2")
    implementation("com.google.api-client:google-api-client-android:2.8.1")
    implementation("com.google.api-client:google-api-client-gson:2.8.1")
    implementation("androidx.core:core-splashscreen:1.2.0")
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}