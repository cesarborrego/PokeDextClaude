package com.cesar.pokedexclaude.ui.screens.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cesar.pokedexclaude.domain.model.PokemonType
import com.cesar.pokedexclaude.core.ui.theme.PokemonTypeColors.toTypeColor

/**
 * Displays a Pokémon type as a colored chip/badge.
 *
 * @param type The Pokémon type to display
 * @param modifier Modifier to be applied to the chip
 */
@Composable
fun PokemonTypeChip(
    type: PokemonType,
    modifier: Modifier = Modifier
) {
    Text(
        text = type.typeName.uppercase(),
        modifier = modifier
            .background(
                color = type.typeName.toTypeColor(),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp),
        color = Color.White,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelSmall
    )
}

@Preview(showBackground = true)
@Composable
private fun PokemonTypeChipPreview() {
    PokemonTypeChip(type = PokemonType.FIRE)
}

@Preview(showBackground = true)
@Composable
private fun PokemonTypeChipWaterPreview() {
    PokemonTypeChip(type = PokemonType.WATER)
}
