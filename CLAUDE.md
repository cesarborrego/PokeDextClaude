# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

PokedexClaude is an Android application built with Kotlin and Jetpack Compose. The project uses modern Android development practices with Material3 design components.

**Stack:**
- Language: Kotlin 2.2.10
- UI Framework: Jetpack Compose (BOM 2026.02.01)
- Build System: Gradle with Kotlin DSL (.kts files)
- Min SDK: 24, Target SDK: 36
- Architecture: Single Activity with Compose

## Build Commands

### Build and Run
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install and run on connected device/emulator
./gradlew installDebug

# Clean build
./gradlew clean
```

### Testing
```bash
# Run unit tests
./gradlew test

# Run specific unit test class
./gradlew test --tests com.cesar.pokedexclaude.ExampleUnitTest

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Run specific instrumented test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.cesar.pokedexclaude.ExampleInstrumentedTest
```

### Code Quality
```bash
# Lint check
./gradlew lint

# Generate lint report
./gradlew lintDebug
```

## Project Structure

```
app/src/
├── main/
│   ├── java/com/cesar/pokedexclaude/
│   │   ├── MainActivity.kt           # Single Activity entry point
│   │   └── ui/theme/                 # Compose theme configuration
│   │       ├── Color.kt
│   │       ├── Theme.kt
│   │       └── Type.kt
│   ├── res/                          # Android resources
│   └── AndroidManifest.xml
├── test/                             # Unit tests (JUnit)
└── androidTest/                      # Instrumented tests (Espresso)
```

## Architecture Notes

### Compose UI
- Single Activity design pattern with `MainActivity` as entry point
- Edge-to-edge display enabled via `enableEdgeToEdge()`
- Material3 design system with custom theme (`PokedexClaudeTheme`)
- Theme configuration located in `ui/theme/` package

### Dependency Management
- Uses Version Catalog (TOML) at `gradle/libs.versions.toml`
- All dependencies referenced via `libs` accessor in build files
- Compose BOM manages all Compose library versions consistently

### When Adding Dependencies
1. Add version to `[versions]` section in `gradle/libs.versions.toml`
2. Add library to `[libraries]` section using version reference
3. Reference in `app/build.gradle.kts` via `libs.` prefix (e.g., `implementation(libs.androidx.core.ktx)`)

### Kotlin Configuration
- Java compatibility: Java 11
- Kotlin code style: official
- Compose compiler plugin enabled via `kotlin-compose` plugin

## Development Notes

### Running the App
- Requires Android Studio or connected Android device/emulator
- Use `./gradlew installDebug` to install, or run from Android Studio
- App namespace: `com.cesar.pokedexclaude`

### Compose Previews
- Use `@Preview` annotation for Composable previews in Android Studio
- Preview composables should be standalone and not require runtime data

### Testing Strategy
- Unit tests in `test/` directory use JUnit 4
- Instrumented tests in `androidTest/` use AndroidJUnit4 runner
- Compose UI testing available via `androidx.compose.ui.test.junit4`