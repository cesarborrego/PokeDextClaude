package com.cesar.pokedexclaude.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from the Pokémon list endpoint (GET /pokemon)
 *
 * Single Responsibility Principle: Only represents API response structure
 */
@Serializable
data class PokemonListResponse(
    @SerialName("count")
    val count: Int,
    @SerialName("next")
    val next: String?,
    @SerialName("previous")
    val previous: String?,
    @SerialName("results")
    val results: List<PokemonListItemDto>,
)

/**
 * Individual Pokémon item in the list response
 */
@Serializable
data class PokemonListItemDto(
    @SerialName("name")
    val name: String,
    @SerialName("url")
    val url: String,
) {
    /**
     * Extracts the Pokémon ID from the URL
     * Example URL: "https://pokeapi.co/api/v2/pokemon/1/"
     */
    val id: Int
        get() =
            url
                .trimEnd('/')
                .split('/')
                .last()
                .toInt()
}
