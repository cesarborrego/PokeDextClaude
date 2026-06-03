package com.cesar.pokedexclaude.ui.screens.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cesar.pokedexclaude.domain.model.Pokemon
import com.cesar.pokedexclaude.domain.model.PokemonType

/**
 * Displays a single Pokémon item in the list with modern, friendly design.
 *
 * Design improvements:
 * - Increased corner radius (20dp) for friendlier appearance
 * - Subtle shadow with refined elevation
 * - Improved spacing and visual hierarchy
 * - Pokemon ID in primary color for brand consistency
 * - Larger, more prominent Pokemon name
 *
 * Accessibility:
 * - Minimum 48dp touch target height
 * - Semantic content descriptions
 * - High contrast text on surface background
 *
 * @param pokemon The Pokémon to display
 * @param onClick Callback invoked when the item is clicked
 * @param modifier Modifier to be applied to the card
 */
@Composable
fun PokemonListItem(
    pokemon: Pokemon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.large, // 20dp rounded corners
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pokémon sprite image with rounded corners
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = "${pokemon.name} sprite",
                modifier = Modifier
                    .size(88.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Pokémon info column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Pokémon ID in primary color
                Text(
                    text = "#${pokemon.id.toString().padStart(3, '0')}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                // Pokémon name - larger and more prominent
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Type chips with refined spacing
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pokemon.types.forEach { type ->
                        PokemonTypeChip(type = type)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
@Composable
private fun PokemonListItemPreview() {
    MaterialTheme {
        PokemonListItem(
            pokemon = Pokemon(
                id = 25,
                name = "Pikachu",
                imageUrl = "",
                types = listOf(PokemonType.ELECTRIC)
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAFAFA)
@Composable
private fun PokemonListItemDualTypePreview() {
    MaterialTheme {
        PokemonListItem(
            pokemon = Pokemon(
                id = 6,
                name = "Charizard",
                imageUrl = "",
                types = listOf(PokemonType.FIRE, PokemonType.FLYING)
            ),
            onClick = {}
        )
    }
}
