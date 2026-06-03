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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cesar.pokedexclaude.domain.model.PokemonStat

/**
 * Displays a Pokémon stat with a progress bar visualization.
 * Shows stat name, value, and a progress bar indicating the stat relative to max (255).
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
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Stat value
            Text(
                text = stat.baseStat.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Progress bar showing stat relative to max base stat (255)
        LinearProgressIndicator(
            progress = { (stat.baseStat / 255f).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonStatBarPreview() {
    PokemonStatBar(
        stat = PokemonStat(
            name = "Attack",
            baseStat = 120
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PokemonStatBarLowPreview() {
    PokemonStatBar(
        stat = PokemonStat(
            name = "HP",
            baseStat = 45
        )
    )
}
