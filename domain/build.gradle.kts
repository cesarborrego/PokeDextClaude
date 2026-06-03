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
    // Core common module for Result wrapper
    implementation(projects.core.common)

    // Coroutines (for suspend functions)
    implementation(libs.kotlinx.coroutines.core)

    // Dependency Injection
    implementation(libs.koin.core)

    // Testing
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
    testImplementation("io.mockk:mockk:1.13.8")
}
