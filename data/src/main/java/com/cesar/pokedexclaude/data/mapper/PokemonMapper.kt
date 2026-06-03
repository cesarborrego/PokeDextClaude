package com.cesar.pokedexclaude.data.mapper

import com.cesar.pokedexclaude.data.remote.dto.PokemonDto
import com.cesar.pokedexclaude.data.remote.dto.PokemonSpeciesDto
import com.cesar.pokedexclaude.domain.model.Pokemon
import com.cesar.pokedexclaude.domain.model.PokemonDetail
import com.cesar.pokedexclaude.domain.model.PokemonStat
import com.cesar.pokedexclaude.domain.model.PokemonType

/**
 * Mapper object to convert API DTOs to domain models.
 *
 * This class demonstrates the Single Responsibility Principle:
 * - Only responsible for data transformation (DTO -> Domain)
 * - No network calls, no business logic, no UI concerns
 *
 * Benefits:
 * - Easy to test: pure functions with predictable input/output
 * - Centralized transformation logic
 * - Domain models remain independent of API structure
 */
object PokemonMapper {

    /**
     * Maps a PokemonDto to a Pokémon domain model for list display.
     *
     * @param dto The DTO from the API
     * @return Pokémon domain model with basic information
     */
    fun mapToPokemon(dto: PokemonDto): Pokemon {
        return Pokemon(
            id = dto.id,
            name = dto.name.capitalizeWords(),
            imageUrl = dto.sprites.frontDefault ?: "",
            types = dto.types
                .sortedBy { it.slot }
                .map { PokemonType.fromString(it.type.name) }
        )
    }

    /**
     * Maps PokemonDto and PokemonSpeciesDto to a PokemonDetail domain model.
     * Combines data from both endpoints to create complete Pokémon details.
     *
     * @param pokemonDto The Pokémon detail DTO
     * @param speciesDto The Pokémon species DTO (for description)
     * @return PokemonDetail domain model with comprehensive information
     */
    fun mapToPokemonDetail(
        pokemonDto: PokemonDto,
        speciesDto: PokemonSpeciesDto
    ): PokemonDetail {
        return PokemonDetail(
            id = pokemonDto.id,
            name = pokemonDto.name.capitalizeWords(),
            // Prefer high-quality official artwork, fallback to front sprite
            imageUrl = pokemonDto.sprites.other?.officialArtwork?.frontDefault
                ?: pokemonDto.sprites.frontDefault
                ?: "",
            types = pokemonDto.types
                .sortedBy { it.slot }
                .map { PokemonType.fromString(it.type.name) },
            height = pokemonDto.height,
            weight = pokemonDto.weight,
            stats = pokemonDto.stats.map { statDto ->
                PokemonStat(
                    name = formatStatName(statDto.stat.name),
                    baseStat = statDto.baseStat
                )
            },
            abilities = pokemonDto.abilities.map { abilitySlot ->
                abilitySlot.ability.name.capitalizeWords()
            },
            description = extractEnglishDescription(speciesDto)
        )
    }

    /**
     * Formats stat names from API format to display format.
     * Example: "special-attack" -> "Sp. Atk"
     */
    private fun formatStatName(statName: String): String {
        return when (statName) {
            "hp" -> "HP"
            "attack" -> "Attack"
            "defense" -> "Defense"
            "special-attack" -> "Sp. Atk"
            "special-defense" -> "Sp. Def"
            "speed" -> "Speed"
            else -> statName.capitalizeWords()
        }
    }

    /**
     * Extracts the English description from species flavor text entries.
     * Cleans up formatting characters (newlines, form feeds).
     *
     * @param speciesDto The species DTO containing flavor text entries
     * @return Cleaned English description, or empty string if not found
     */
    private fun extractEnglishDescription(speciesDto: PokemonSpeciesDto): String {
        val englishEntry = speciesDto.flavorTextEntries
            .firstOrNull { it.language.name == "en" }

        return englishEntry?.flavorText
            ?.replace("\n", " ")
            ?.replace("\u000c", " ")
            ?.replace("  ", " ")
            ?.trim()
            ?: ""
    }

    /**
     * Capitalizes each word in a string (e.g., "pikachu" -> "Pikachu").
     * Handles hyphenated names (e.g., "mr-mime" -> "Mr-Mime").
     */
    private fun String.capitalizeWords(): String {
        return split("-")
            .joinToString("-") { word ->
                word.replaceFirstChar { it.uppercase() }
            }
    }
}
