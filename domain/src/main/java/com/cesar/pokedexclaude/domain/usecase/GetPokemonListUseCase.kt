package com.cesar.pokedexclaude.domain.usecase

import com.cesar.pokedexclaude.domain.model.Pokemon
import com.cesar.pokedexclaude.domain.repository.PokemonRepository

/**
 * Use case for fetching a paginated list of Pokémon.
 *
 * This use case encapsulates the business logic for retrieving Pokémon lists,
 * providing a clean interface between the UI layer and the data layer.
 *
 * Following the Single Responsibility Principle, this class has one job:
 * coordinate the retrieval of Pokémon lists through the repository.
 *
 * @param repository The repository providing data access
 */
class GetPokemonListUseCase(
    private val repository: PokemonRepository
) {
    /**
     * Executes the use case to fetch a paginated list of Pokémon.
     *
     * @param limit Number of Pokémon to fetch (default: 20)
     * @param offset Starting position in the list (default: 0)
     * @return Result containing list of Pokémon or an error
     */
    suspend operator fun invoke(
        limit: Int = DEFAULT_PAGE_SIZE,
        offset: Int = 0
    ): Result<List<Pokemon>> {
        // Input validation
        require(limit > 0) { "Limit must be greater than 0" }
        require(offset >= 0) { "Offset must be non-negative" }

        // Delegate to repository
        return repository.getPokemonList(limit, offset)
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}
