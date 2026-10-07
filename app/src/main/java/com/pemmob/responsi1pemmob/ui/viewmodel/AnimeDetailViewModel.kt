package com.pemmob.responsi1pemmob.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.responsi1pemmob.data.repository.AnimeRepository
import com.pemmob.responsi1pemmob.ui.state.AnimeDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnimeDetailViewModel(
    private val repository: AnimeRepository = AnimeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnimeDetailUiState>(AnimeDetailUiState.Loading)
    val uiState: StateFlow<AnimeDetailUiState> = _uiState.asStateFlow()

    fun loadAnimeDetail(animeId: Int) {
        _uiState.value = AnimeDetailUiState.Loading
        viewModelScope.launch {
            repository.getAnimeDetail(animeId).onSuccess { anime ->
                _uiState.value = AnimeDetailUiState.Success(anime)
            }.onFailure { error ->
                _uiState.value = AnimeDetailUiState.Error(
                    error.localizedMessage ?: "Gagal memuat detail anime."
                )
            }
        }
    }
}
