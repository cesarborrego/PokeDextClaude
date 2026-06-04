// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.detekt)
}

// Apply ktlint and detekt to all subprojects
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.6.0")
        android.set(true)
        outputToConsole.set(true)
        ignoreFailures.set(false)

        // Filter out generated files
        filter {
            exclude { element -> element.file.path.contains("generated/") }
            exclude { element -> element.file.path.contains("build/") }
        }
    }

    configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        allRules = false
        config.setFrom(rootProject.file("config/detekt/detekt.yml"))
        baseline = file("${project.projectDir}/detekt-baseline.xml")
    }
}

// Global Detekt configuration
detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/config/detekt/detekt.yml")
    baseline = file("$projectDir/config/detekt/baseline.xml")
}

// Configure Detekt reports on tasks
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
    }
}

// Dependency for Detekt formatting rules
dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
}

// Task to run all quality checks
tasks.register("qualityCheck") {
    group = "verification"
    description = "Runs all quality checks (ktlint, detekt, lint)"

    subprojects {
        this@register.dependsOn(tasks.matching { it.name == "ktlintCheck" })
        this@register.dependsOn(tasks.matching { it.name == "detekt" })
    }
}

// Task to auto-format code
tasks.register("formatCode") {
    group = "formatting"
    description = "Auto-formats code using ktlint"

    subprojects {
        this@register.dependsOn(tasks.matching { it.name == "ktlintFormat" })
    }
}

// Task to check only modified files (fast)
tasks.register("quickCheck") {
    group = "verification"
    description = "Quick checks on modified files"

    subprojects {
        this@register.dependsOn(tasks.matching { it.name == "ktlintCheck" })
    }
}

// Pre-commit task (run before committing)
tasks.register("preCommit") {
    group = "verification"
    description = "Runs all checks suitable for pre-commit hook"

    subprojects {
        this@register.dependsOn(tasks.matching { it.name == "ktlintCheck" })
        this@register.dependsOn(tasks.matching { it.name == "detekt" })
    }
}