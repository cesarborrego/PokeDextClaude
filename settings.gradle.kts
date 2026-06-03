pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// Enable type-safe project accessors for better dependency management
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "PokedexClaude"
include(":app")
include(":core:common")
include(":core:network")
include(":core:ui")
include(":domain")
include(":data")
include(":myapplication")
