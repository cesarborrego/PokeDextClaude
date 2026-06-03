package com.cesar.pokedexclaude.domain.model

/**
 * Enum representing all 18 Pokémon types.
 * This is a pure domain model without UI concerns (colors are handled in the UI layer).
 */
enum class PokemonType(val typeName: String) {
    NORMAL("Normal"),
    FIRE("Fire"),
    WATER("Water"),
    ELECTRIC("Electric"),
    GRASS("Grass"),
    ICE("Ice"),
    FIGHTING("Fighting"),
    POISON("Poison"),
    GROUND("Ground"),
    FLYING("Flying"),
    PSYCHIC("Psychic"),
    BUG("Bug"),
    ROCK("Rock"),
    GHOST("Ghost"),
    DRAGON("Dragon"),
    DARK("Dark"),
    STEEL("Steel"),
    FAIRY("Fairy");

    companion object {
        /**
         * Converts a string type name to a PokemonType enum value.
         * Case-insensitive matching.
         *
         * @param typeName The type name as a string
         * @return The corresponding PokemonType enum, or NORMAL if not found
         */
        fun fromString(typeName: String): PokemonType {
            return entries.find {
                it.typeName.equals(typeName, ignoreCase = true)
            } ?: NORMAL
        }
    }
}
