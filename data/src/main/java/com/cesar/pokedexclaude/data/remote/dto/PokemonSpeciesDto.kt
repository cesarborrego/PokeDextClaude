package com.cesar.pokedexclaude.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from the Pokémon species endpoint (GET /pokemon-species/{id})
 * Contains flavor text (descriptions) and other species-specific information.
 */
@Serializable
data class PokemonSpeciesDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntryDto>
)

/**
 * Flavor text entry (Pokedex description) in various languages
 */
@Serializable
data class FlavorTextEntryDto(
    @SerialName("flavor_text")
    val flavorText: String,
    @SerialName("language")
    val language: LanguageDto
)

/**
 * Language information
 */
@Serializable
data class LanguageDto(
    @SerialName("name")
    val name: String
)
