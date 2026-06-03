package com.cesar.pokedexclaude.ui.screens.list

/**
 * Represents user intentions/actions for the Pokémon list screen.
 * In MVI architecture, intents are user actions that trigger state changes.
 */
sealed interface PokemonListIntent {
    /**
     * Intent to load the initial list of Pokémon.
     * Triggered when the screen is first displayed or when user pulls to refresh.
     */
    data object LoadInitial : PokemonListIntent

    /**
     * Intent to load more Pokémon for pagination.
     * Triggered when user scrolls to the end of the list.
     */
    data object LoadMore : PokemonListIntent

    /**
     * Intent to update the search query and filter the Pokémon list.
     * Triggered when user types in the search field.
     *
     * @property query The search query entered by the user
     */
    data class SearchQueryChanged(val query: String) : PokemonListIntent

    /**
     * Intent to retry loading Pokémon after an error.
     * Triggered when user taps the retry button on error screen.
     */
    data object Retry : PokemonListIntent
}