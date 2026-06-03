package com.cesar.pokedexclaude.ui.screens.detail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Displays a Pokémon ability as a refined chip with subtle border.
 *
 * Design improvements:
 * - Rounded corners (14dp) for friendly appearance
 * - Subtle border for definition without heavy shadows
 * - Secondary container color for visual distinction
 * - Improved padding for better proportions
 * - Medium font weight for readability
 *
 * Accessibility:
 * - High contrast text on container background
 * - Readable font size
 * - Clear visual boundaries
 *
 * @param abilityName The name of the ability to display
 * @param modifier Modifier to be applied to the chip
 */
@Composable
fun PokemonAbilityChip(
    abilityName: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.small
            ),
        shape = MaterialTheme.shapes.small, // 12dp rounded corners
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 0.dp // Flat design, border provides definition
    ) {
        Text(
            text = abilityName,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonAbilityChipPreview() {
    MaterialTheme {
        PokemonAbilityChip(abilityName = "Overgrow")
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonAbilityChipLongPreview() {
    MaterialTheme {
        PokemonAbilityChip(abilityName = "Chlorophyll")
    }
}
