package com.cesar.pokedexclaude.data.repository

import com.cesar.pokedexclaude.core.common.util.Result
import com.cesar.pokedexclaude.data.mapper.PokemonMapper
import com.cesar.pokedexclaude.data.remote.api.PokeApiService
import com.cesar.pokedexclaude.domain.model.Pokemon
import com.cesar.pokedexclaude.domain.model.PokemonDetail
import com.cesar.pokedexclaude.domain.repository.PokemonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * Implementation of Pokémon Repository using the PokeAPI service.
 * Handles API calls, error handling, and data transformation.
 *
 * This class demonstrates the Dependency Inversion Principle (SOLID):
 * - The interface PokemonRepository is defined in :domain (high-level)
 * - This implementation in :data depends on that interface (low-level depends on high-level)
 *
 * @property apiService The Retrofit API service for making network requests
 */
class PokemonRepositoryImpl(
    private val apiService: PokeApiService,
) : PokemonRepository {
    /**
     * Fetches a paginated list of Pokémon with full details.
     * First fetches the list, then makes parallel requests for each Pokémon's details.
     *
     * This method demonstrates:
     * - Single Responsibility: Only handles data fetching
     * - Error handling: Graceful degradation if individual Pokemon fail
     */
    override suspend fun getPokemonList(
        limit: Int,
        offset: Int,
    ): Result<List<Pokemon>> =
        try {
            // Fetch the list of Pokémon (names and URLs)
            val listResponse = apiService.getPokemonList(limit, offset)

            // Fetch details for each Pokémon in parallel using coroutineScope
            val pokemonList =
                coroutineScope {
                    listResponse.results
                        .map { listItem ->
                            async {
                                try {
                                    val detailDto = apiService.getPokemonDetail(listItem.id)
                                    PokemonMapper.mapToPokemon(detailDto)
                                } catch (e: Exception) {
                                    // If a single Pokémon fails, log and skip it
                                    null
                                }
                            }
                        }.mapNotNull { it.await() }
                }

            Result.Success(pokemonList)
        } catch (e: Exception) {
            Result.Error(
                exception = e,
                message = "Failed to fetch Pokémon list: ${e.message}",
            )
        }

    /**
     * Fetches detailed information about a specific Pokémon.
     * Makes parallel requests to both Pokémon and Species endpoints.
     */
    override suspend fun getPokemonDetail(id: Int): Result<PokemonDetail> =
        try {
            // Fetch Pokémon detail and species info in parallel
            val (pokemonDto, speciesDto) =
                coroutineScope {
                    val pokemonDeferred = async { apiService.getPokemonDetail(id) }
                    val speciesDeferred = async { apiService.getPokemonSpecies(id) }

                    Pair(pokemonDeferred.await(), speciesDeferred.await())
                }

            val pokemonDetail = PokemonMapper.mapToPokemonDetail(pokemonDto, speciesDto)
            Result.Success(pokemonDetail)
        } catch (e: Exception) {
            Result.Error(
                exception = e,
                message = "Failed to fetch Pokémon detail: ${e.message}",
            )
        }
}
