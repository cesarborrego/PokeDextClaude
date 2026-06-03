package com.cesar.pokedexclaude.ui.screens.list

import com.cesar.pokedexclaude.domain.model.Pokemon

/**
 * Sealed interface representing all possible states of the Pokémon list screen.
 * Uses sealed interface for cleaner syntax and better performance.
 *
 * Note: Search and pagination state is kept in the Success state because:
 * 1. These states can coexist (success + search + loading more)
 * 2. Search is a UI-level concern that doesn't affect the core loading state
 * 3. Pagination (loading more) is an incremental operation on existing success state
 *
 * Using sealed classes instead of nullable fields with boolean flags adheres to:
 * - Type Safety: Compiler enforces exhaustive when expressions
 * - Single Responsibility: Each state encapsulates only relevant data
 * - Make Invalid States Unrepresentable: Cannot have contradictory state combinations
 */
sealed interface PokemonListUiState {
    /**
     * Initial loading state when first opening the screen.
     */
    data object Loading : PokemonListUiState

    /**
     * Success state with loaded Pokémon list.
     * All list-related state is guaranteed to be valid when in this state.
     *
     * @property pokemonList The filtered list of Pokémon to display
     * @property allPokemonList The complete unfiltered list
     * @property searchQuery Current search query
     * @property isLoadingMore True when loading additional pages
     * @property currentOffset Current pagination offset
     */
    data class Success(
        val pokemonList: List<Pokemon>,
        val allPokemonList: List<Pokemon>,
        val searchQuery: String = "",
        val isLoadingMore: Boolean = false,
        val currentOffset: Int = 0,
    ) : PokemonListUiState {
        companion object {
            const val PAGE_SIZE = 20
        }
    }

    /**
     * Error state when initial loading fails.
     * @property message The error message to display
     */
    data class Error(
        val message: String,
    ) : PokemonListUiState
}
