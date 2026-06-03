package com.cesar.pokedexclaude.ui.screens.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cesar.pokedexclaude.ui.common.ErrorView
import com.cesar.pokedexclaude.ui.common.LoadingView
import com.cesar.pokedexclaude.ui.screens.list.components.PokemonListItem
import org.koin.androidx.compose.koinViewModel

/**
 * Main Pokémon list screen displaying a scrollable list of Pokémon.
 * Supports pagination - automatically loads more Pokémon when scrolling to the bottom.
 * Uses MVI (Model-View-Intent) architecture pattern with sealed classes for type-safe state management.
 *
 * @param onPokemonClick Callback invoked when a Pokémon is clicked, receives Pokémon ID
 * @param modifier Modifier to be applied to the screen
 * @param viewModel The MVI ViewModel managing the screen state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    onPokemonClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonListMviViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pokédex",
                        fontWeight = FontWeight.Bold,
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        // Exhaustive when expression with sealed class - compiler verifies all cases are handled
        when (val currentState = state) {
            is PokemonListUiState.Loading -> {
                LoadingView()
            }

            is PokemonListUiState.Error -> {
                // No !! operator - data guaranteed non-null in Error state
                ErrorView(
                    message = currentState.message,
                    onRetry = {
                        viewModel.processIntent(PokemonListIntent.Retry)
                    },
                )
            }

            is PokemonListUiState.Success -> {
                // No !! operator - all data guaranteed non-null in Success state
                Column(
                    modifier =
                        Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                ) {
                    // Search bar - state guaranteed to be Success
                    SearchBar(
                        searchQuery = currentState.searchQuery,
                        onSearchQueryChange = { query ->
                            viewModel.processIntent(PokemonListIntent.SearchQueryChanged(query))
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                    )

                    PokemonList(
                        pokemonList = currentState.pokemonList,
                        onPokemonClick = onPokemonClick,
                        onLoadMore = {
                            viewModel.processIntent(PokemonListIntent.LoadMore)
                        },
                        isLoadingMore = currentState.isLoadingMore,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

/**
 * Displays the list of Pokémon with pagination support.
 * Shows a loading indicator at the bottom when loading more items.
 * All data is guaranteed to be non-null when this composable is called.
 */
@Composable
private fun PokemonList(
    pokemonList: List<com.cesar.pokedexclaude.domain.model.Pokemon>,
    onPokemonClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    // Detect when we've scrolled near the bottom to trigger pagination
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItemIndex =
                listState.layoutInfo.visibleItemsInfo
                    .lastOrNull()
                    ?.index ?: 0
            val totalItemsCount = listState.layoutInfo.totalItemsCount
            lastVisibleItemIndex >= totalItemsCount - 5
        }
    }

    // Trigger load more when we're near the bottom
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = pokemonList,
            key = { pokemon -> pokemon.id },
        ) { pokemon ->
            PokemonListItem(
                pokemon = pokemon,
                onClick = { onPokemonClick(pokemon.id) },
            )
        }

        // Loading indicator for pagination - only show when actually loading more
        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}

/**
 * Search bar for filtering Pokémon by name.
 * Displays a text field with a search icon and a clear button when text is entered.
 *
 * @param searchQuery Current search query text
 * @param onSearchQueryChange Callback invoked when search query changes
 * @param modifier Modifier to be applied to the search bar
 */
@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = modifier,
        placeholder = { Text("Buscar Pokémon...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
            )
        },
        trailingIcon = {
            // Show clear button only when there's text
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar búsqueda",
                    )
                }
            }
        },
        singleLine = true,
    )
}
