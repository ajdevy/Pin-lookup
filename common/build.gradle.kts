plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.flexsentlabs.myapplication.common"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
        targetSdk = 36
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    
    testImplementation(libs.junit)
}
