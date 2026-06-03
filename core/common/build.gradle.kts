plugins {
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    // Coroutines (for suspend functions in domain layer)
    implementation(libs.kotlinx.coroutines.core)

    // Testing
    testImplementation(libs.junit)
}
