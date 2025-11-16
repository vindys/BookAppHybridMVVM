plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.feature_booklist"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    configurations.all {
        resolutionStrategy {
            force ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            force ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
            force ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        }
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.txt"
            )
        }
    }
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.runner)
    androidTestImplementation(libs.espresso.core)

    // Common library for network, database, common ui
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
    debugImplementation(libs.androidx.ui.tooling)
    ksp(libs.hiltCompiler)

    // Paging
    implementation(libs.pagingRuntime)
    implementation(libs.pagingCompose)

    // Coroutines
    implementation(libs.coroutinesCore)
    implementation(libs.coroutinesAndroid)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.material3)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofitGson)

    // Room
    implementation(libs.roomRuntime)
    implementation(libs.roomKtx)
    ksp(libs.roomCompiler)

    // Hilt
    implementation(libs.hiltAndroid)
    ksp(libs.hiltCompiler)

    implementation(libs.mockk)
    testImplementation(libs.coroutinesTest)
    testImplementation(libs.turbine)
    testImplementation(kotlin("test"))
}