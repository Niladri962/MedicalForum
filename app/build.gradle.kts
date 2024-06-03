plugins {
    alias(libs.plugins.android.application)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.smedicalapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smedicalapp"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))


    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    //Firestore dependecies
    implementation("com.google.firebase:firebase-firestore")

    //authentication dependencies
    implementation("com.google.firebase:firebase-auth")

    //storage dependencies
    implementation("com.google.firebase:firebase-storage")

    //bumptech glide
    //https://github.com/bumptech/glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //annotation processor
    //https://bumptech.github.io/glide/doc/download-setup.html
    annotationProcessor ("com.github.bumptech.glide:compiler:4.14.2")
}