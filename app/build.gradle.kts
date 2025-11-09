plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.bookapphybridmvvm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.bookapphybridmvvm"
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14" // matches your Compose compiler
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.material)
    implementation (libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":feature_booklist"))
    implementation(project(":core"))

    // Compose
    implementation(libs.composeUi)
    implementation(libs.composeMaterial)
    implementation(libs.composePreview)
    implementation(libs.navigationCompose)

    // Lifecycle + ViewModel
    implementation (libs.lifecycleRuntime)
    implementation(libs.lifecycleViewModelCompose)

    // Hilt + Compose
    implementation(libs.hiltAndroid)
    implementation(libs.hiltNavigationCompose)
    ksp(libs.hiltCompiler)

    // Paging
    implementation(libs.pagingRuntime)
    implementation(libs.pagingCompose)

    // Retrofit + Gson
    implementation(libs.retrofit)
    implementation(libs.retrofitGson)

    // Apollo GraphQL
    implementation(libs.apolloRuntime)

    // Room
    implementation(libs.roomRuntime)
    implementation(libs.roomKtx)
    ksp(libs.roomCompiler)

    // Coroutines
    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.coroutinesTest)
    testImplementation(libs.turbine)
}