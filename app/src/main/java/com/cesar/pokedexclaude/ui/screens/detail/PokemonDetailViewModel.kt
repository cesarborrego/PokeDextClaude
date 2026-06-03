package com.cesar.pokedexclaude.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cesar.pokedexclaude.domain.repository.PokemonRepository
import com.cesar.pokedexclaude.domain.model.PokemonDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Pokémon detail screen.
 * Manages the detailed Pokémon data and loading states.cd
 *
 * @property repository Repository for accessing Pokémon data (injected via Koin)
 */
class PokemonDetailViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Loading)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    /**
     * Loads detailed information for a specific Pokémon.
     *
     * @param pokemonId The ID of the Pokémon to load
     */
    fun loadPokemonDetail(pokemonId: Int) {
        viewModelScope.launch {
            _uiState.value = PokemonDetailUiState.Loading

            repository.getPokemonDetail(pokemonId)
                .onSuccess { pokemonDetail ->
                    _uiState.value = PokemonDetailUiState.Success(pokemonDetail)
                }
                .onFailure { error ->
                    _uiState.value = PokemonDetailUiState.Error(
                        error.message ?: "An unknown error occurred"
                    )
                }
        }
    }
}

/**
 * UI state for the Pokémon detail screen.
 */
sealed interface PokemonDetailUiState {
    /**
     * Loading state - displayed when fetching Pokémon details.
     */
    data object Loading : PokemonDetailUiState

    /**
     * Success state - displayed when Pokémon detail data is available.
     *
     * @property pokemonDetail The detailed Pokémon information
     */
    data class Success(val pokemonDetail: PokemonDetail) : PokemonDetailUiState

    /**
     * Error state - displayed when data fetching fails.
     *
     * @property message The error message to display
     */
    data class Error(val message: String) : PokemonDetailUiState
}
