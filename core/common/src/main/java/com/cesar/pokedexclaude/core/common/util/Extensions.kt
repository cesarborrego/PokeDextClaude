package com.cesar.pokedexclaude.core.common.util

/**
 * Common Kotlin extensions used across the application.
 * These extensions provide utility functions that can be reused in any layer.
 */

/**
 * Extension function to convert a nullable String to an empty string if null.
 *
 * Example:
 * ```
 * val nullableString: String? = null
 * val result = nullableString.orEmpty() // Returns ""
 * ```
 */
fun String?.orEmptyIfNull(): String = this ?: ""

/**
 * Extension function to capitalize the first character of a string.
 *
 * Example:
 * ```
 * "pikachu".capitalizeFirstChar() // Returns "Pikachu"
 * ```
 */
fun String.capitalizeFirstChar(): String =
    this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }

/**
 * Extension function to check if a string is a valid URL.
 */
fun String.isValidUrl(): Boolean = this.startsWith("http://") || this.startsWith("https://")
