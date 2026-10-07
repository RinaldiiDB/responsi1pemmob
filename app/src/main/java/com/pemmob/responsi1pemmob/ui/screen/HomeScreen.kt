package com.pemmob.responsi1pemmob.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import com.pemmob.responsi1pemmob.ui.components.AnimeGridItem
import com.pemmob.responsi1pemmob.ui.components.AnimeListItem
import com.pemmob.responsi1pemmob.ui.components.EmptyView
import com.pemmob.responsi1pemmob.ui.components.ErrorView
import com.pemmob.responsi1pemmob.ui.components.GenreFilterChips
import com.pemmob.responsi1pemmob.ui.components.LoadingView
import com.pemmob.responsi1pemmob.ui.state.AnimeUiState
import com.pemmob.responsi1pemmob.ui.state.LayoutMode
import com.pemmob.responsi1pemmob.ui.theme.Poppins
import com.pemmob.responsi1pemmob.ui.viewmodel.AnimeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AnimeViewModel,
    onAnimeClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedGenreId by viewModel.selectedGenreId.collectAsState()
    val genres by viewModel.genres.collectAsState()
    val layoutMode by viewModel.layoutMode.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "AKUWIBU",
                            style = MaterialTheme.typography.titleLarge,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Temukan Anime Favoritmu",
                            style = MaterialTheme.typography.labelMedium,
                            fontFamily = Poppins,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleLayoutMode() }) {
                        Icon(
                            imageVector = if (layoutMode == LayoutMode.GRID) Icons.AutoMirrored.Filled.List else Icons.Default.GridView,
                            contentDescription = "Toggle Layout"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Cari judul anime...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.clearSearch() }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Search"
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            GenreFilterChips(
                genres = genres,
                selectedGenreId = selectedGenreId,
                onGenreSelected = { viewModel.onGenreSelected(it) },
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (val state = uiState) {
                is AnimeUiState.Loading -> {
                    LoadingView()
                }

                is AnimeUiState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = { viewModel.fetchAnime() }
                    )
                }

                is AnimeUiState.Success -> {
                    if (state.animeList.isEmpty()) {
                        EmptyView(message = "Anime tidak ditemukan. Coba kata kunci lain.")
                    } else if (layoutMode == LayoutMode.GRID) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.animeList,
                                key = { it.malId }
                            ) { anime ->
                                AnimeGridItem(
                                    anime = anime,
                                    onClick = { onAnimeClick(anime.malId) }
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = state.animeList,
                                key = { it.malId }
                            ) { anime ->
                                AnimeListItem(
                                    anime = anime,
                                    onClick = { onAnimeClick(anime.malId) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
