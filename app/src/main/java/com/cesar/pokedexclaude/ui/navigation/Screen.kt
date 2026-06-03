package com.cesar.pokedexclaude.ui.navigation

/**
 * Sealed class representing all navigation destinations in the app.
 * Each screen has a route that is used by the Navigation component.
 */
sealed class Screen(val route: String) {

    /**
     * Pokémon list screen - the main screen showing all Pokémon.
     */
    data object PokemonList : Screen("pokemon_list")

    /**
     * Pokémon detail screen - shows detailed information about a single Pokémon.
     * Route includes a path parameter for the Pokémon ID.
     */
    data object PokemonDetail : Screen("pokemon_detail/{pokemonId}") {
        /**
         * Creates the navigation route for a specific Pokémon.
         *
         * @param pokemonId The ID of the Pokémon to display
         * @return The complete route with the ID parameter
         */
        fun createRoute(pokemonId: Int): String {
            return "pokemon_detail/$pokemonId"
        }
    }
}
