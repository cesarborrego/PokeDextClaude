package com.cesar.pokedexclaude.core.ui.shimmer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * Applies a diagonal shimmer (sweep light) effect to any composable.
 *
 * The gradient is projected in window coordinates so a single shared [getProgress]
 * value produces a continuous wave that travels across the whole screen, not just
 * within the bounds of each individual placeholder.
 *
 * @param colors      Three-stop color list: [base, highlight, base].
 * @param getProgress Lambda returning the current animation progress (0f–1f).
 *                    Read inside the draw phase to avoid recomposition on every frame.
 * @param bandWidth   Width (px) of the traveling highlight band.
 */
fun Modifier.shimmerEffect(
    colors: List<Color>,
    getProgress: () -> Float,
    bandWidth: Float = 500f,
): Modifier = composed {
    var bounds by remember { mutableStateOf(Rect.Zero) }

    val screenSizePx = with(LocalDensity.current) {
        with(LocalConfiguration.current) {
            (screenWidthDp + screenHeightDp).dp.toPx()
        }
    }

    this
        .onGloballyPositioned { coords ->
            bounds = coords.boundsInWindow()
        }
        .drawWithContent {
            val offset = screenSizePx * getProgress()
            val startX = -bandWidth + offset - bounds.left
            val startY = -bandWidth + offset - bounds.top
            drawRect(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = Offset(startX, startY),
                    end = Offset(startX + bandWidth, startY + bandWidth),
                ),
            )
        }
}
