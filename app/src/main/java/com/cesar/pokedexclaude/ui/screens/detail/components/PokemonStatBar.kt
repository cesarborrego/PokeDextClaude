package com.cesar.pokedexclaude.ui.screens.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cesar.pokedexclaude.domain.model.PokemonStat

/**
 * Displays a Pokémon stat with a modern progress bar visualization.
 *
 * Design improvements:
 * - Thicker progress bar (8dp) for better visibility
 * - Rounded corners on progress bar
 * - Stat value in primary color with SemiBold weight
 * - Improved spacing and alignment
 * - Better visual hierarchy between label and value
 *
 * Shows stat name, value, and a progress bar indicating the stat relative to max (255).
 *
 * Accessibility:
 * - Clear visual distinction between label and value
 * - High contrast colors
 * - Meaningful semantic structure
 *
 * @param stat The Pokémon stat to display
 * @param modifier Modifier to be applied to the column
 */
@Composable
fun PokemonStatBar(
    stat: PokemonStat,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Stat name
            Text(
                text = stat.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Stat value in primary color
            Text(
                text = stat.baseStat.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Thicker, rounded progress bar
        LinearProgressIndicator(
            progress = { (stat.baseStat / 255f).coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(MaterialTheme.shapes.extraSmall), // Rounded ends
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonStatBarPreview() {
    MaterialTheme {
        PokemonStatBar(
            stat = PokemonStat(
                name = "Attack",
                baseStat = 120
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonStatBarLowPreview() {
    MaterialTheme {
        PokemonStatBar(
            stat = PokemonStat(
                name = "HP",
                baseStat = 45
            )
        )
    }
}
