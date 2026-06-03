package com.cesar.pokedexclaude.di

import com.cesar.pokedexclaude.ui.screens.detail.PokemonDetailViewModel
import com.cesar.pokedexclaude.ui.screens.list.PokemonListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module providing ViewModel dependencies.
 * Uses viewModel factory for proper Android ViewModel lifecycle management.
 */
val viewModelModule = module {

    /**
     * Provides factory instance of PokemonListViewModel.
     * Injects PokemonRepository from repositoryModule.
     *
     * Factory scope is used because:
     * - ViewModels should be recreated for each screen instance
     * - ViewModels hold UI-specific state that shouldn't be shared
     * - Each screen/navigation destination needs its own ViewModel instance
     */
    viewModel {
        PokemonListViewModel(
            repository = get() // Injects PokemonRepository from Koin
        )
    }

    /**
     * Provides factory instance of PokemonDetailViewModel.
     * Injects PokemonRepository from repositoryModule.
     */
    viewModel {
        PokemonDetailViewModel(
            repository = get() // Injects PokemonRepository from Koin
        )
    }
}
