package com.cesar.pokedexclaude.data.remote.api

import com.cesar.pokedexclaude.data.remote.dto.PokemonDto
import com.cesar.pokedexclaude.data.remote.dto.PokemonListResponse
import com.cesar.pokedexclaude.data.remote.dto.PokemonSpeciesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for the PokeAPI.
 * Base URL: https://pokeapi.co/api/v2/
 *
 * This interface demonstrates:
 * - Interface Segregation Principle: Only Pokemon-related endpoints
 * - Single Responsibility: Only defines API contract, no implementation
 */
interface PokeApiService {
    /**
     * Fetches a paginated list of Pokémon.
     *
     * @param limit Number of Pokémon to fetch (default: 20)
     * @param offset Starting position in the list (default: 0)
     * @return Response containing a list of Pokémon names and URLs
     */
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
    ): PokemonListResponse

    /**
     * Fetches detailed information about a specific Pokémon.
     *
     * @param id The Pokémon's Pokedex number
     * @return Detailed Pokémon data including stats, types, and abilities
     */
    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int,
    ): PokemonDto

    /**
     * Fetches species information for a specific Pokémon.
     * Used to get the Pokedex description text.
     *
     * @param id The Pokémon's Pokedex number
     * @return Species data including flavor text entries
     */
    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(
        @Path("id") id: Int,
    ): PokemonSpeciesDto
}
