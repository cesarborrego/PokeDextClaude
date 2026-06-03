package com.cesar.pokedexclaude.domain.model

/**
 * Represents detailed information about a Pokémon, including all stats and abilities.
 *
 * @property id The Pokémon's Pokedex number
 * @property name The Pokémon's name
 * @property imageUrl URL to the Pokémon's sprite image
 * @property types List of the Pokémon's types (1-2 types)
 * @property height Height in decimeters (1 decimeter = 0.1 meters)
 * @property weight Weight in hectograms (1 hectogram = 0.1 kilograms)
 * @property stats List of all base stats (HP, Attack, Defense, etc.)
 * @property abilities List of ability names this Pokémon can have
 * @property description Flavor text description from the Pokedex
 */
data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<PokemonType>,
    val height: Int,
    val weight: Int,
    val stats: List<PokemonStat>,
    val abilities: List<String>,
    val description: String
)
