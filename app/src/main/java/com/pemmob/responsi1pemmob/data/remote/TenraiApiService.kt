package com.pemmob.responsi1pemmob.data.remote

import com.pemmob.responsi1pemmob.data.model.AnimeDetailResponse
import com.pemmob.responsi1pemmob.data.model.AnimeResponse
import com.pemmob.responsi1pemmob.data.model.GenreResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TenraiApiService {

    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25
    ): AnimeResponse

    @GET("anime")
    suspend fun searchAnime(
        @Query("q") query: String? = null,
        @Query("genres") genres: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 25,
        @Query("order_by") orderBy: String = "score",
        @Query("sort") sort: String = "desc"
    ): AnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetail(
        @Path("id") id: Int
    ): AnimeDetailResponse

    @GET("genres/anime")
    suspend fun getAnimeGenres(): GenreResponse
}
