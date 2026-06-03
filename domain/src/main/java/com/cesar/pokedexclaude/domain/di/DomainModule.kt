package com.cesar.pokedexclaude.domain.di

import com.cesar.pokedexclaude.domain.usecase.GetPokemonDetailUseCase
import com.cesar.pokedexclaude.domain.usecase.GetPokemonListUseCase
import org.koin.dsl.module

/**
 * Koin module providing domain layer dependencies.
 *
 * This module configures dependency injection for use cases.
 * Use cases are provided as factories since they're typically short-lived
 * and created on-demand by ViewModels.
 */
val domainModule = module {

    /**
     * Provides GetPokemonListUseCase.
     * Factory scope ensures a new instance is created each time it's injected.
     */
    factory {
        GetPokemonListUseCase(repository = get())
    }

    /**
     * Provides GetPokemonDetailUseCase.
     * Factory scope ensures a new instance is created each time it's injected.
     */
    factory {
        GetPokemonDetailUseCase(repository = get())
    }
}
