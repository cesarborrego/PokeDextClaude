package com.cesar.pokedexclaude.ui.screens.list.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cesar.pokedexclaude.core.ui.shimmer.shimmerEffect

private val nameWidthVariants = listOf(140.dp, 120.dp, 160.dp, 130.dp, 150.dp, 125.dp)

@Composable
fun PokemonListShimmer(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val shimmerColors = remember(colorScheme) {
        listOf(
            colorScheme.onSurface.copy(alpha = 0.10f),
            colorScheme.onSurface.copy(alpha = 0.20f),
            colorScheme.onSurface.copy(alpha = 0.10f),
        )
    }

    val transition = rememberInfiniteTransition(label = "shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerProgress",
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .semantics { contentDescription = "Cargando Pokémon" }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(6) { index ->
            ShimmerListItem(
                shimmerColors = shimmerColors,
                getProgress = { progress },
                nameWidth = nameWidthVariants[index],
            )
        }
    }
}

@Composable
private fun ShimmerListItem(
    shimmerColors: List<Color>,
    getProgress: () -> Float,
    nameWidth: Dp = 140.dp,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmerEffect(shimmerColors, getProgress),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(12.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(shimmerColors, getProgress),
                )

                Box(
                    modifier = Modifier
                        .width(nameWidth)
                        .height(22.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerEffect(shimmerColors, getProgress),
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(20.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .shimmerEffect(shimmerColors, getProgress),
                        )
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PokemonListShimmerPreview() {
    MaterialTheme {
        PokemonListShimmer()
    }
}
