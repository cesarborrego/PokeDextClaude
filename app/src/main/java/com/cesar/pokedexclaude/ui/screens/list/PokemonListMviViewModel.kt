package com.cesar.pokedexclaude.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.pokedexclaude.domain.model.Pokemon
import com.cesar.pokedexclaude.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * MVI ViewModel for the Pokémon list screen.
 * Implements the Model-View-Intent pattern with unidirectional data flow.
 *
 * State management uses sealed classes to ensure type safety and prevent invalid states.
 * All state transitions are explicit and traceable.
 *
 * @property repository Repository for accessing Pokémon data (injected via Koin)
 */
class PokemonListMviViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val state: StateFlow<PokemonListUiState> = _state.asStateFlow()

    init {
        // Load initial data when ViewModel is created
        processIntent(PokemonListIntent.LoadInitial)
    }

    /**
     * Processes user intents and updates the state accordingly.
     * This is the single entry point for all user actions in MVI architecture.
     *
     * @param intent The user intent to process
     */
    fun processIntent(intent: PokemonListIntent) {
        when (intent) {
            is PokemonListIntent.LoadInitial -> handleLoadInitial()
            is PokemonListIntent.LoadMore -> handleLoadMore()
            is PokemonListIntent.SearchQueryChanged -> handleSearchQueryChanged(intent.query)
            is PokemonListIntent.Retry -> handleRetry()
        }
    }

    /**
     * Handles the LoadInitial intent by loading the first page of Pokémon.
     * Sets loading state, fetches data, and updates state with results or error.
     *
     * State transitions:
     * Loading/Error -> Loading -> Success/Error
     */
    private fun handleLoadInitial() {
        viewModelScope.launch {
            // Set loading state
            _state.value = PokemonListUiState.Loading

            // Fetch data from repository
            repository.getPokemonList(
                limit = PokemonListUiState.Success.PAGE_SIZE,
                offset = 0
            )
                .onSuccess { pokemonList ->
                    // Success state - all data guaranteed non-null
                    _state.value = PokemonListUiState.Success(
                        pokemonList = pokemonList,
                        allPokemonList = pokemonList,
                        searchQuery = "",
                        isLoadingMore = false,
                        currentOffset = PokemonListUiState.Success.PAGE_SIZE
                    )
                }
                .onFailure { error ->
                    // Error state with message
                    _state.value = PokemonListUiState.Error(
                        message = error.message ?: "An unknown error occurred"
                    )
                }
        }
    }

    /**
     * Handles the LoadMore intent for pagination.
     * Type-safe: Only loads if in Success state and not already loading.
     *
     * State transitions:
     * Success -> Success(isLoadingMore=true) -> Success(with updated data)
     */
    private fun handleLoadMore() {
        val currentState = _state.value

        // Type-safe guard: Only load more if in Success state and not already loading
        if (currentState !is PokemonListUiState.Success || currentState.isLoadingMore) {
            return
        }

        viewModelScope.launch {
            // Update to loading more state
            _state.value = currentState.copy(isLoadingMore = true)

            // Fetch more data from repository
            repository.getPokemonList(
                limit = PokemonListUiState.Success.PAGE_SIZE,
                offset = currentState.currentOffset
            )
                .onSuccess { newPokemon ->
                    // Update state with new data appended to existing list
                    val updatedAllList = currentState.allPokemonList + newPokemon
                    _state.value = currentState.copy(
                        isLoadingMore = false,
                        allPokemonList = updatedAllList,
                        pokemonList = filterPokemonList(updatedAllList, currentState.searchQuery),
                        currentOffset = currentState.currentOffset + PokemonListUiState.Success.PAGE_SIZE
                    )
                }
                .onFailure { error ->
                    // Keep existing data, just stop loading indicator
                    _state.value = currentState.copy(isLoadingMore = false)
                    // In production, you might want to show a snackbar or toast
                    println("Failed to load more Pokémon: ${error.message}")
                }
        }
    }

    /**
     * Handles the SearchQueryChanged intent by updating the search query
     * and filtering the Pokémon list accordingly.
     *
     * Type-safe: Only processes search in Success state where data exists.
     *
     * @param query The new search query
     */
    private fun handleSearchQueryChanged(query: String) {
        val currentState = _state.value

        // Type-safe guard: Only process search if in Success state
        if (currentState !is PokemonListUiState.Success) return

        _state.value = currentState.copy(
            searchQuery = query,
            pokemonList = filterPokemonList(currentState.allPokemonList, query)
        )
    }

    /**
     * Handles the Retry intent by attempting to load data again.
     * This is triggered when the user taps retry after an error.
     *
     * State transitions:
     * Error -> Loading -> Success/Error
     */
    private fun handleRetry() {
        handleLoadInitial()
    }

    /**
     * Filters the Pokémon list based on the search query.
     * Returns all Pokémon if query is empty, otherwise filters by name (case-insensitive).
     *
     * @param pokemonList The complete list of Pokémon to filter
     * @param query The search query to filter by
     * @return Filtered list of Pokémon
     */
    private fun filterPokemonList(pokemonList: List<Pokemon>, query: String): List<Pokemon> {
        val trimmedQuery = query.trim()

        if (trimmedQuery.isEmpty()) {
            return pokemonList
        }

        // Filter by name - case insensitive
        return pokemonList.filter { pokemon ->
            pokemon.name.contains(trimmedQuery, ignoreCase = true)
        }
    }
}