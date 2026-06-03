package com.cesar.pokedexclaude.domain.model

/**
 * Represents a single Pokémon stat (HP, Attack, Defense, etc.)
 *
 * @property name The name of the stat (e.g., "HP", "Attack", "Defense")
 * @property baseStat The base value of the stat (typically 0-255)
 */
data class PokemonStat(
    val name: String,
    val baseStat: Int
)
