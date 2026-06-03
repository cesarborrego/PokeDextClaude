package com.cesar.pokedexclaude.ui.screens.detail.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Displays a Pokémon ability as a chip.
 * Uses a surface with secondary container color for visual distinction.
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
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.secondaryContainer,
        tonalElevation = 2.dp
    ) {
        Text(
            text = abilityName,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonAbilityChipPreview() {
    PokemonAbilityChip(abilityName = "Overgrow")
}
