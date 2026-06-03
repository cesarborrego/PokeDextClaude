package com.cesar.pokedexclaude

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.cesar.pokedexclaude.ui.navigation.PokedexNavigation
import com.cesar.pokedexclaude.core.ui.theme.PokedexClaudeTheme

/**
 * Main activity for the Pokedex app.
 * Displays the Pokémon list and detail screens using Jetpack Compose navigation.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexClaudeTheme {
                PokedexNavigation()
            }
        }
    }
}