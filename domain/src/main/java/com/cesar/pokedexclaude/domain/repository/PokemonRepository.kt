package com.cesar.pokedexclaude.domain.repository

import com.cesar.pokedexclaude.domain.model.Pokemon
import com.cesar.pokedexclaude.domain.model.PokemonDetail

/**
 * Repository interface for Pokémon data operations.
 * Provides an abstraction layer between the data sources and the use cases/ViewModels.
 *
 * This interface is defined in the domain layer following the Dependency Inversion Principle.
 * The implementation resides in the data layer.
 */
interface PokemonRepository {

    /**
     * Fetches a paginated list of Pokémon with full details.
     * Makes parallel requests to fetch details for each Pokémon in the list.
     *
     * @param limit Number of Pokémon to fetch
     * @param offset Starting position in the list
     * @return Result containing list of Pokémon or an error
     */
    suspend fun getPokemonList(limit: Int, offset: Int): Result<List<Pokemon>>

    /**
     * Fetches detailed information about a specific Pokémon.
     * Combines data from Pokémon detail and species endpoints.
     *
     * @param id The Pokémon's Pokedex number
     * @return Result containing PokemonDetail or an error
     */
    suspend fun getPokemonDetail(id: Int): Result<PokemonDetail>
}
