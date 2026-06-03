package com.cesar.pokedexclaude.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cesar.pokedexclaude.ui.screens.detail.PokemonDetailScreen
import com.cesar.pokedexclaude.ui.screens.list.PokemonListScreen

/**
 * Main navigation composable that sets up the NavHost and defines all navigation routes.
 *
 * @param modifier Modifier to be applied to the NavHost
 * @param navController The navigation controller (default: creates new instance)
 */
@Composable
fun PokedexNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route,
        modifier = modifier
    ) {
        // Pokémon List Screen
        composable(route = Screen.PokemonList.route) {
            PokemonListScreen(
                onPokemonClick = { pokemonId ->
                    navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
                }
            )
        }

        // Pokémon Detail Screen
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(
                navArgument("pokemonId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getInt("pokemonId") ?: return@composable
            PokemonDetailScreen(
                pokemonId = pokemonId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
