package com.cesar.pokedexclaude.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Pokemon type color mappings for UI display.
 * Colors are based on the official Pokémon type color scheme.
 *
 * This is kept in the UI layer to maintain separation of concerns.
 * The domain layer doesn't know about colors - that's a UI concern.
 */
object PokemonTypeColors {
    val Normal = Color(0xFFA8A878)
    val Fire = Color(0xFFF08030)
    val Water = Color(0xFF6890F0)
    val Electric = Color(0xFFF8D030)
    val Grass = Color(0xFF78C850)
    val Ice = Color(0xFF98D8D8)
    val Fighting = Color(0xFFC03028)
    val Poison = Color(0xFFA040A0)
    val Ground = Color(0xFFE0C068)
    val Flying = Color(0xFFA890F0)
    val Psychic = Color(0xFFF85888)
    val Bug = Color(0xFFA8B820)
    val Rock = Color(0xFFB8A038)
    val Ghost = Color(0xFF705898)
    val Dragon = Color(0xFF7038F8)
    val Dark = Color(0xFF705848)
    val Steel = Color(0xFFB8B8D0)
    val Fairy = Color(0xFFEE99AC)

    /**
     * Extension function to get the display color for a Pokemon type.
     * This bridges the domain model with UI concerns.
     *
     * Usage:
     * ```
     * val type = PokemonType.FIRE
     * val color = type.toColor()
     * ```
     */
    fun String.toTypeColor(): Color {
        return when (this.lowercase()) {
            "normal" -> Normal
            "fire" -> Fire
            "water" -> Water
            "electric" -> Electric
            "grass" -> Grass
            "ice" -> Ice
            "fighting" -> Fighting
            "poison" -> Poison
            "ground" -> Ground
            "flying" -> Flying
            "psychic" -> Psychic
            "bug" -> Bug
            "rock" -> Rock
            "ghost" -> Ghost
            "dragon" -> Dragon
            "dark" -> Dark
            "steel" -> Steel
            "fairy" -> Fairy
            else -> Normal
        }
    }
}
