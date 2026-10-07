package com.pemmob.responsi1pemmob.data.repository

import com.pemmob.responsi1pemmob.data.model.Anime
import com.pemmob.responsi1pemmob.data.model.Genre
import com.pemmob.responsi1pemmob.data.remote.ApiClient
import com.pemmob.responsi1pemmob.data.remote.TenraiApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnimeRepository(
    private val apiService: TenraiApiService = ApiClient.apiService
) {

    suspend fun getTopAnime(): Result<List<Anime>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTopAnime()
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun searchAnime(
        query: String? = null,
        genreId: Int? = null
    ): Result<List<Anime>> = withContext(Dispatchers.IO) {
        try {
            val genreParam = genreId?.toString()
            val queryParam = query?.takeIf { it.isNotBlank() }
            
            val response = if (queryParam == null && genreParam == null) {
                apiService.getTopAnime()
            } else {
                apiService.searchAnime(
                    query = queryParam,
                    genres = genreParam
                )
            }
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAnimeDetail(id: Int): Result<Anime> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAnimeDetail(id)
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGenres(): Result<List<Genre>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAnimeGenres()
            Result.success(response.data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
