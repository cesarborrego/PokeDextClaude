package com.cesar.pokedexclaude.ui.screens.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cesar.pokedexclaude.domain.model.PokemonDetail
import com.cesar.pokedexclaude.ui.common.ErrorView
import com.cesar.pokedexclaude.ui.common.LoadingView
import com.cesar.pokedexclaude.ui.screens.detail.components.PokemonAbilityChip
import com.cesar.pokedexclaude.ui.screens.detail.components.PokemonStatBar
import com.cesar.pokedexclaude.ui.screens.list.components.PokemonTypeChip
import org.koin.androidx.compose.koinViewModel

/**
 * Pokémon detail screen showing comprehensive information about a single Pokémon.
 * Displays image, description, stats, abilities, types, height, and weight.
 * Uses MVI (Model-View-Intent) architecture pattern with sealed classes for type-safe state management.
 *
 * @param pokemonId The ID of the Pokémon to display
 * @param onNavigateBack Callback invoked when the back button is clicked
 * @param modifier Modifier to be applied to the screen
 * @param viewModel The MVI ViewModel managing the screen state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailMviViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    // Load Pokémon details when screen is first composed
    LaunchedEffect(pokemonId) {
        viewModel.processIntent(PokemonDetailIntent.LoadDetail(pokemonId))
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    // Type-safe title extraction - no !! operator needed
                    val title =
                        when (val currentState = state) {
                            is PokemonDetailUiState.Success -> currentState.pokemonDetail.name
                            else -> "Pokemon Detail"
                        }
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        // Exhaustive when expression with sealed class - compiler verifies all cases are handled
        when (val currentState = state) {
            is PokemonDetailUiState.Idle -> {
                // Show nothing or a placeholder while waiting for intent
            }

            is PokemonDetailUiState.Loading -> {
                LoadingView(modifier = Modifier.padding(paddingValues))
            }

            is PokemonDetailUiState.Error -> {
                // No !! operator - data guaranteed non-null in Error state
                ErrorView(
                    message = currentState.message,
                    onRetry = {
                        viewModel.processIntent(PokemonDetailIntent.Retry)
                    },
                    modifier = Modifier.padding(paddingValues),
                )
            }

            is PokemonDetailUiState.Success -> {
                // No !! operator - data guaranteed non-null in Success state
                PokemonDetailContent(
                    pokemonDetail = currentState.pokemonDetail,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}

/**
 * Displays the detailed Pokémon information in a scrollable column.
 * All data is guaranteed to be non-null when this composable is called.
 */
@Composable
private fun PokemonDetailContent(
    pokemonDetail: PokemonDetail,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Pokémon Image
        item {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = pokemonDetail.imageUrl,
                    contentDescription = pokemonDetail.name,
                    modifier = Modifier.size(250.dp),
                )
            }
        }

        // Pokedex Number
        item {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "#${pokemonDetail.id.toString().padStart(3, '0')}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        // Types
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                pokemonDetail.types.forEach { type ->
                    PokemonTypeChip(type = type)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        // Description Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = pokemonDetail.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        // Physical Info Card (Height & Weight)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    // Height
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Height",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("%.1f m", pokemonDetail.height / 10.0),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    // Weight
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Weight",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("%.1f kg", pokemonDetail.weight / 10.0),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }

        // Stats Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Base Stats",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    pokemonDetail.stats.forEach { stat ->
                        PokemonStatBar(stat = stat)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Abilities Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Abilities",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        pokemonDetail.abilities.forEach { ability ->
                            PokemonAbilityChip(abilityName = ability)
                        }
                    }
                }
            }
        }
    }
}
