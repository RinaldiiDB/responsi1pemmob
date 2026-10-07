package com.pemmob.responsi1pemmob.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.responsi1pemmob.data.model.Genre
import com.pemmob.responsi1pemmob.data.repository.AnimeRepository
import com.pemmob.responsi1pemmob.ui.state.AnimeUiState
import com.pemmob.responsi1pemmob.ui.state.LayoutMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnimeViewModel(
    private val repository: AnimeRepository = AnimeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnimeUiState>(AnimeUiState.Loading)
    val uiState: StateFlow<AnimeUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedGenreId = MutableStateFlow<Int?>(null)
    val selectedGenreId: StateFlow<Int?> = _selectedGenreId.asStateFlow()

    private val _genres = MutableStateFlow<List<Genre>>(emptyList())
    val genres: StateFlow<List<Genre>> = _genres.asStateFlow()

    private val _layoutMode = MutableStateFlow(LayoutMode.GRID)
    val layoutMode: StateFlow<LayoutMode> = _layoutMode.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        fetchGenres()
        fetchAnime()
    }

    private fun fetchGenres() {
        viewModelScope.launch {
            repository.getGenres().onSuccess { genreList ->
                _genres.value = genreList.take(20)
            }
        }
    }

    fun fetchAnime() {
        _uiState.value = AnimeUiState.Loading
        viewModelScope.launch {
            val result = repository.searchAnime(
                query = _searchQuery.value,
                genreId = _selectedGenreId.value
            )
            result.onSuccess { animeList ->
                if (animeList.isEmpty()) {
                    _uiState.value = AnimeUiState.Error("Anime tidak ditemukan. Coba kata kunci atau filter lain.")
                } else {
                    _uiState.value = AnimeUiState.Success(animeList)
                }
            }.onFailure { error ->
                _uiState.value = AnimeUiState.Error(
                    error.localizedMessage ?: "Gagal memuat data anime. Periksa koneksi internet Anda."
                )
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(600)
            fetchAnime()
        }
    }

    fun onGenreSelected(genreId: Int?) {
        if (_selectedGenreId.value == genreId) {
            _selectedGenreId.value = null
        } else {
            _selectedGenreId.value = genreId
        }
        fetchAnime()
    }

    fun clearSearch() {
        _searchQuery.value = ""
        fetchAnime()
    }

    fun toggleLayoutMode() {
        _layoutMode.value = if (_layoutMode.value == LayoutMode.GRID) {
            LayoutMode.LIST
        } else {
            LayoutMode.GRID
        }
    }
}
