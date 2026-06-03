package com.cesar.pokedexclaude.ui.screens.detail

/**
 * Represents user intentions/actions for the Pokémon detail screen.
 * In MVI architecture, intents are user actions that trigger state changes.
 */
sealed interface PokemonDetailIntent {
    /**
     * Intent to load detailed information for a specific Pokémon.
     * Triggered when the screen is displayed.
     *
     * @property pokemonId The ID of the Pokémon to load details for
     */
    data class LoadDetail(val pokemonId: Int) : PokemonDetailIntent

    /**
     * Intent to retry loading Pokémon details after an error.
     * Triggered when user taps the retry button on error screen.
     */
    data object Retry : PokemonDetailIntent
}