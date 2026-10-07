package com.pemmob.responsi1pemmob.ui.state

import com.pemmob.responsi1pemmob.data.model.Anime

sealed interface AnimeUiState {
    object Loading : AnimeUiState
    data class Success(val animeList: List<Anime>) : AnimeUiState
    data class Error(val message: String) : AnimeUiState
}

sealed interface AnimeDetailUiState {
    object Loading : AnimeDetailUiState
    data class Success(val anime: Anime) : AnimeDetailUiState
    data class Error(val message: String) : AnimeDetailUiState
}

enum class LayoutMode {
    LIST,
    GRID
}
