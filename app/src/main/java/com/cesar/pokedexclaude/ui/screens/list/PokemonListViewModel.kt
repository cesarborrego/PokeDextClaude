package com.cesar.pokedexclaude.ui.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.pokedexclaude.domain.repository.PokemonRepository
import com.cesar.pokedexclaude.domain.model.Pokemon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Pokémon list screen.
 * Manages the list of Pokémon, pagination, and loading states.
 *
 * @property repository Repository for accessing Pokémon data (injected via Koin)
 */
class PokemonListViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    // Search query state - exposed to the UI
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 20
    private var isLoadingMore = false

    // Store all loaded Pokémon for filtering
    private var allPokemonList: List<Pokemon> = emptyList()

    init {
        loadPokemon()
    }

    /**
     * Loads the initial set of Pokémon.
     */
    fun loadPokemon() {
        viewModelScope.launch {
            _uiState.value = PokemonListUiState.Loading
            currentOffset = 0

            repository.getPokemonList(limit = pageSize, offset = currentOffset)
                .onSuccess { pokemonList ->
                    allPokemonList = pokemonList
                    _uiState.value = PokemonListUiState.Success(filterPokemonList())
                    currentOffset += pageSize
                }
                .onFailure { error ->
                    _uiState.value = PokemonListUiState.Error(
                        error.message ?: "An unknown error occurred"
                    )
                }
        }
    }

    /**
     * Loads more Pokémon for pagination.
     * Only loads if not already loading and in success state.
     */
    fun loadMore() {
        if (isLoadingMore) return

        val currentState = _uiState.value
        if (currentState !is PokemonListUiState.Success) return

        viewModelScope.launch {
            isLoadingMore = true

            repository.getPokemonList(limit = pageSize, offset = currentOffset)
                .onSuccess { newPokemon ->
                    allPokemonList = allPokemonList + newPokemon
                    _uiState.value = PokemonListUiState.Success(filterPokemonList())
                    currentOffset += pageSize
                }
                .onFailure { error ->
                    // Keep the current list, just show an error for the new page
                    // In a production app, you might want to show a toast or snackbar
                    println("Failed to load more Pokémon: ${error.message}")
                }

            isLoadingMore = false
        }
    }

    /**
     * Updates the search query and filters the Pokémon list accordingly.
     * Filters by Pokémon name (case-insensitive).
     *
     * @param query The search query to filter by
     */
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query

        // Update UI state with filtered list
        val currentState = _uiState.value
        if (currentState is PokemonListUiState.Success) {
            _uiState.value = PokemonListUiState.Success(filterPokemonList())
        }
    }

    /**
     * Filters the complete Pokémon list based on the current search query.
     * Returns all Pokémon if query is empty, otherwise filters by name (case-insensitive).
     *
     * @return Filtered list of Pokémon
     */
    private fun filterPokemonList(): List<Pokemon> {
        val query = _searchQuery.value.trim()

        if (query.isEmpty()) {
            return allPokemonList
        }

        // Filter by name - case insensitive
        return allPokemonList.filter { pokemon ->
            pokemon.name.contains(query, ignoreCase = true)
        }
    }
}

/**
 * UI state for the Pokémon list screen.
 */
sealed interface PokemonListUiState {
    /**
     * Loading state - displayed when fetching initial data.
     */
    data object Loading : PokemonListUiState

    /**
     * Success state - displayed when Pokémon data is available.
     *
     * @property pokemonList The list of Pokémon to display
     */
    data class Success(val pokemonList: List<Pokemon>) : PokemonListUiState

    /**
     * Error state - displayed when data fetching fails.
     *
     * @property message The error message to display
     */
    data class Error(val message: String) : PokemonListUiState
}
