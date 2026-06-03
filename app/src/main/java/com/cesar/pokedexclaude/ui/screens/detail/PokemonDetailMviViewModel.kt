package com.cesar.pokedexclaude.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.pokedexclaude.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * MVI ViewModel for the Pokémon detail screen.
 * Implements the Model-View-Intent pattern with unidirectional data flow.
 *
 * State management uses sealed classes to ensure type safety and prevent invalid states.
 * All state transitions are explicit and traceable.
 *
 * @property repository Repository for accessing Pokémon data (injected via Koin)
 */
class PokemonDetailMviViewModel(
    private val repository: PokemonRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Idle)
    val state: StateFlow<PokemonDetailUiState> = _state.asStateFlow()

    /**
     * Processes user intents and updates the state accordingly.
     * This is the single entry point for all user actions in MVI architecture.
     *
     * @param intent The user intent to process
     */
    fun processIntent(intent: PokemonDetailIntent) {
        when (intent) {
            is PokemonDetailIntent.LoadDetail -> handleLoadDetail(intent.pokemonId)
            is PokemonDetailIntent.Retry -> handleRetry()
        }
    }

    /**
     * Handles the LoadDetail intent by loading detailed information for a Pokémon.
     * Sets loading state, fetches data, and updates state with results or error.
     *
     * State transitions:
     * Idle/Error -> Loading -> Success/Error
     *
     * @param pokemonId The ID of the Pokémon to load details for
     */
    private fun handleLoadDetail(pokemonId: Int) {
        viewModelScope.launch {
            // Set loading state - now type-safe with pokemonId included
            _state.value = PokemonDetailUiState.Loading(pokemonId)

            // Fetch data from repository
            repository
                .getPokemonDetail(pokemonId)
                .onSuccess { pokemonDetail ->
                    // Success state - no nullable fields, guaranteed non-null data
                    _state.value = PokemonDetailUiState.Success(pokemonDetail)
                }.onFailure { error ->
                    // Error state - includes pokemonId for retry functionality
                    _state.value =
                        PokemonDetailUiState.Error(
                            message = error.message ?: "An unknown error occurred",
                            pokemonId = pokemonId,
                        )
                }
        }
    }

    /**
     * Handles the Retry intent by attempting to load Pokémon details again.
     * This is triggered when the user taps retry after an error.
     *
     * Type-safe extraction of pokemonId from current state:
     * - Only retries if in Error or Loading state (which both contain pokemonId)
     * - Success state doesn't need retry
     * - Idle state has no pokemonId to retry with
     */
    private fun handleRetry() {
        when (val currentState = _state.value) {
            is PokemonDetailUiState.Error -> {
                handleLoadDetail(currentState.pokemonId)
            }

            is PokemonDetailUiState.Loading -> {
                handleLoadDetail(currentState.pokemonId)
            }

            is PokemonDetailUiState.Success -> {
                // Already have data, no need to retry
            }

            is PokemonDetailUiState.Idle -> {
                // No pokemonId to retry with, do nothing
            }
        }
    }
}
