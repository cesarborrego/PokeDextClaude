package com.cesar.pokedexclaude.ui.screens.detail

import com.cesar.pokedexclaude.domain.model.PokemonDetail

/**
 * Sealed interface representing all possible states of the Pokémon detail screen.
 * This approach makes invalid states unrepresentable and provides compile-time
 * guarantees that all states are handled in the UI.
 *
 * Using sealed classes instead of nullable fields with boolean flags adheres to:
 * - Type Safety: Compiler enforces exhaustive when expressions
 * - Single Responsibility: Each state encapsulates only relevant data
 * - Make Invalid States Unrepresentable: Cannot have contradictory state combinations
 *
 * References:
 * - Kotlin Coding Conventions: Use sealed classes for restricted class hierarchies
 * - Clean Architecture: State should clearly represent domain concepts
 */
sealed interface PokemonDetailUiState {

    /**
     * Initial idle state before any data is loaded.
     * Displayed momentarily before LoadDetail intent is processed.
     */
    data object Idle : PokemonDetailUiState

    /**
     * Loading state while fetching Pokémon details.
     * @property pokemonId The ID being loaded (stored for retry logic)
     */
    data class Loading(val pokemonId: Int) : PokemonDetailUiState

    /**
     * Success state with loaded Pokémon details.
     * All data is guaranteed to be non-null when in this state.
     * @property pokemonDetail The fully loaded Pokémon information
     */
    data class Success(val pokemonDetail: PokemonDetail) : PokemonDetailUiState

    /**
     * Error state when loading fails.
     * @property message The error message to display
     * @property pokemonId The ID that failed to load (enables retry functionality)
     */
    data class Error(
        val message: String,
        val pokemonId: Int
    ) : PokemonDetailUiState
}