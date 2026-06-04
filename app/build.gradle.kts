plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.cesar.pokedexclaude"
    compileSdk {
        version =
            release(36) {
                minorApiLevel = 1
            }
    }

    defaultConfig {
        applicationId = "com.cesar.pokedexclaude"
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
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }

    lint {
        // Strictness
        abortOnError = true
        checkAllWarnings = true
        warningsAsErrors = false

        // Performance
        checkDependencies = true
        checkGeneratedSources = false

        // Reports
        htmlReport = true
        xmlReport = true
        sarifReport = true

        // Baseline for incremental adoption
        baseline = file("lint-baseline.xml")

        // Critical rules for this project
        enable +=
            setOf(
                "Interoperability",
                "Security",
                "Performance",
                "Correctness",
                "UnusedResources",
                "Compose",
            )

        // Disable noisy rules
        disable +=
            setOf(
                "IconMissingDensityFolder",
                "GoogleAppIndexingWarning",
                "ObsoleteLintCustomCheck",
            )

        // Custom severities
        error +=
            setOf(
                "StopShip",
                "HardcodedText",
                "MissingPermission",
                "InvalidPackage",
            )

        warning +=
            setOf(
                "UnusedResources",
                "IconDensities",
            )
    }
}

// KSP automatically configures the generated source directories
// The generated Koin modules will be available via org.koin.ksp.generated package

dependencies {
    // Data layer - Repository implementations
    implementation(projects.data)

    // Domain layer - Business logic and models
    implementation(projects.domain)

    // Core modules - These provide shared infrastructure
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.ui)

    // Android essentials
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Koin for Dependency Injection
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.core)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
