package com.cesar.pokedexclaude.di

import com.cesar.pokedexclaude.ui.screens.detail.PokemonDetailMviViewModel
import com.cesar.pokedexclaude.ui.screens.list.PokemonListMviViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module providing MVI ViewModel dependencies.
 * Uses viewModel factory for proper Android ViewModel lifecycle management.
 *
 * Note: This module now provides MVI (Model-View-Intent) ViewModels
 * which implement unidirectional data flow pattern.
 */
val viewModelModule = module {

    /**
     * Provides factory instance of PokemonListMviViewModel.
     * Injects PokemonRepository from repositoryModule.
     *
     * Factory scope is used because:
     * - ViewModels should be recreated for each screen instance
     * - ViewModels hold UI-specific state that shouldn't be shared
     * - Each screen/navigation destination needs its own ViewModel instance
     */
    viewModel {
        PokemonListMviViewModel(
            repository = get() // Injects PokemonRepository from Koin
        )
    }

    /**
     * Provides factory instance of PokemonDetailMviViewModel.
     * Injects PokemonRepository from repositoryModule.
     */
    viewModel {
        PokemonDetailMviViewModel(
            repository = get() // Injects PokemonRepository from Koin
        )
    }
}
