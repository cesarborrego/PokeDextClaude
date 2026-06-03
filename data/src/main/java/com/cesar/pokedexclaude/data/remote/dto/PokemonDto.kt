package com.cesar.pokedexclaude.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response from the Pokémon detail endpoint (GET /pokemon/{id})
 */
@Serializable
data class PokemonDto(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("height")
    val height: Int,
    @SerialName("weight")
    val weight: Int,
    @SerialName("sprites")
    val sprites: PokemonSpritesDto,
    @SerialName("types")
    val types: List<PokemonTypeSlotDto>,
    @SerialName("stats")
    val stats: List<PokemonStatDto>,
    @SerialName("abilities")
    val abilities: List<PokemonAbilitySlotDto>
)

/**
 * Sprites/images for the Pokémon
 */
@Serializable
data class PokemonSpritesDto(
    @SerialName("front_default")
    val frontDefault: String?,
    @SerialName("other")
    val other: PokemonSpritesOtherDto? = null
)

/**
 * High-quality artwork sprites
 */
@Serializable
data class PokemonSpritesOtherDto(
    @SerialName("official-artwork")
    val officialArtwork: PokemonOfficialArtworkDto? = null
)

/**
 * Official Pokémon artwork
 */
@Serializable
data class PokemonOfficialArtworkDto(
    @SerialName("front_default")
    val frontDefault: String?
)

/**
 * Pokémon type slot (Pokémon can have 1-2 types)
 */
@Serializable
data class PokemonTypeSlotDto(
    @SerialName("slot")
    val slot: Int,
    @SerialName("type")
    val type: PokemonTypeInfoDto
)

/**
 * Type information
 */
@Serializable
data class PokemonTypeInfoDto(
    @SerialName("name")
    val name: String
)

/**
 * Pokémon stat entry
 */
@Serializable
data class PokemonStatDto(
    @SerialName("base_stat")
    val baseStat: Int,
    @SerialName("stat")
    val stat: PokemonStatInfoDto
)

/**
 * Stat information
 */
@Serializable
data class PokemonStatInfoDto(
    @SerialName("name")
    val name: String
)

/**
 * Pokémon ability slot
 */
@Serializable
data class PokemonAbilitySlotDto(
    @SerialName("ability")
    val ability: PokemonAbilityInfoDto,
    @SerialName("is_hidden")
    val isHidden: Boolean
)

/**
 * Ability information
 */
@Serializable
data class PokemonAbilityInfoDto(
    @SerialName("name")
    val name: String
)
