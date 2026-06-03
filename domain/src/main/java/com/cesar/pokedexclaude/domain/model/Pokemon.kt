package com.cesar.pokedexclaude.domain.model

/**
 * Represents a Pokémon in the list view with basic information.
 *
 * @property id The Pokémon's Pokedex number
 * @property name The Pokémon's name
 * @property imageUrl URL to the Pokémon's sprite image
 * @property types List of the Pokémon's types (1-2 types)
 */
data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<PokemonType>
)
