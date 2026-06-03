package com.cesar.pokedexclaude.data.di

import com.cesar.pokedexclaude.data.remote.api.PokeApiService
import com.cesar.pokedexclaude.data.repository.PokemonRepositoryImpl
import com.cesar.pokedexclaude.domain.repository.PokemonRepository
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Koin module providing data layer dependencies.
 *
 * This module demonstrates the Dependency Inversion Principle (SOLID):
 * - The high-level module (:domain) defines PokemonRepository interface
 * - This low-level module (:data) provides the implementation
 * - Both depend on the abstraction (interface), not on each other
 *
 * Benefits:
 * - Easy to swap implementations (API -> Room, Mock, etc.)
 * - :domain doesn't need to know about Retrofit or API details
 * - Testable: can inject fake repositories
 */
val dataModule = module {

    /**
     * Provides the PokeApiService using Retrofit.
     * Retrofit instance is provided by :core:network module.
     */
    single<PokeApiService> {
        get<Retrofit>().create(PokeApiService::class.java)
    }

    /**
     * Provides singleton instance of PokemonRepository.
     * Binds the interface to its implementation.
     *
     * The repository is a singleton because:
     * - It manages data fetching and can potentially cache data
     * - Multiple instances could lead to inconsistent state
     * - Repositories don't hold UI-specific state
     *
     * Demonstrates Dependency Inversion Principle:
     * - Interface defined in :domain (high-level)
     * - Implementation provided here in :data (low-level)
     */
    single<PokemonRepository> {
        PokemonRepositoryImpl(
            apiService = get()  // Injected by Koin
        )
    }
}
