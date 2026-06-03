package com.cesar.pokedexclaude.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

/**
 * Reusable loading indicator component.
 *
 * Displays a centered circular progress indicator that fills the available space.
 * Typically used when fetching data or performing async operations.
 */
@Composable
fun LoadingView(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@PreviewLightDark
@Composable
private fun LoadingViewPreview() {
    LoadingView()
}
