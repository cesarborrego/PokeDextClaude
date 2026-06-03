package com.cesar.pokedexclaude.ui.screens.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cesar.pokedexclaude.domain.model.PokemonType
import com.cesar.pokedexclaude.core.ui.theme.PokemonTypeColors.toTypeColor
import com.cesar.pokedexclaude.core.ui.theme.PokemonTypeColors.requiresDarkText

/**
 * Displays a Pokémon type as a refined, modern chip/badge.
 *
 * Design improvements:
 * - Fully rounded corners (12dp) for pill shape
 * - Subtle shadow for depth
 * - Refined padding for better proportions
 * - Adaptive text color (dark text for Electric type)
 * - Bold, uppercase text for strong visual presence
 *
 * Accessibility:
 * - High contrast text (white on color, or dark on light)
 * - Readable font size (11sp with bold weight)
 * - Semantic content description via type name
 *
 * @param type The Pokémon type to display
 * @param modifier Modifier to be applied to the chip
 */
@Composable
fun PokemonTypeChip(
    type: PokemonType,
    modifier: Modifier = Modifier
) {
    val backgroundColor = type.typeName.toTypeColor()
    val textColor = if (type.typeName.requiresDarkText()) {
        Color(0xFF1C1C1E) // Dark text for light backgrounds (Electric)
    } else {
        Color.White // White text for all other types
    }

    Text(
        text = type.typeName.uppercase(),
        modifier = modifier
            .shadow(
                elevation = 0.5.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = backgroundColor.copy(alpha = 0.2f),
                spotColor = backgroundColor.copy(alpha = 0.2f)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 6.dp),
        color = textColor,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelSmall,
        letterSpacing = 0.5.sp
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

@Preview(showBackground = true)
@Composable
private fun PokemonTypeChipElectricPreview() {
    PokemonTypeChip(type = PokemonType.ELECTRIC)
}
