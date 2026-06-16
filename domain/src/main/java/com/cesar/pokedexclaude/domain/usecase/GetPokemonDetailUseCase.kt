package com.cesar.pokedexclaude.domain.usecase

import com.cesar.pokedexclaude.core.common.util.Result
import com.cesar.pokedexclaude.domain.model.PokemonDetail
import com.cesar.pokedexclaude.domain.repository.PokemonRepository

/**
 * Use case for fetching detailed information about a specific Pokémon.
 *
 * This use case encapsulates the business logic for retrieving Pokémon details,
 * providing a clean interface between the UI layer and the data layer.
 *
 * Following the Single Responsibility Principle, this class has one job:
 * coordinate the retrieval of Pokémon details through the repository.
 *
 * @param repository The repository providing data access
 */
class GetPokemonDetailUseCase(
    private val repository: PokemonRepository,
) {
    /**
     * Executes the use case to fetch detailed information about a Pokémon.
     *
     * @param pokemonId The Pokémon's Pokedex number (must be positive)
     * @return Result containing PokemonDetail or an error
     */
    suspend operator fun invoke(pokemonId: Int): Result<PokemonDetail> {
        // Input validation
        require(pokemonId > 0) { "Pokemon ID must be positive" }

        // Delegate to repository
        return repository.getPokemonDetail(pokemonId)
    }
}
