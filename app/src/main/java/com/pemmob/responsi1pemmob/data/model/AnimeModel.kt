package com.pemmob.responsi1pemmob.data.model

import com.google.gson.annotations.SerializedName

data class AnimeResponse(
    @SerializedName("data") val data: List<Anime>,
    @SerializedName("pagination") val pagination: Pagination? = null
)

data class AnimeDetailResponse(
    @SerializedName("data") val data: Anime
)

data class GenreResponse(
    @SerializedName("data") val data: List<Genre>
)

data class Anime(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("title_english") val titleEnglish: String? = null,
    @SerializedName("title_japanese") val titleJapanese: String? = null,
    @SerializedName("images") val images: AnimeImages? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("episodes") val episodes: Int? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("score") val score: Double? = null,
    @SerializedName("scored_by") val scoredBy: Int? = null,
    @SerializedName("rank") val rank: Int? = null,
    @SerializedName("popularity") val popularity: Int? = null,
    @SerializedName("synopsis") val synopsis: String? = null,
    @SerializedName("rating") val rating: String? = null,
    @SerializedName("genres") val genres: List<Genre>? = emptyList()
) {
    val displayImageUrl: String
        get() = images?.jpg?.largeImageUrl
            ?: images?.webp?.largeImageUrl
            ?: images?.jpg?.imageUrl
            ?: images?.webp?.imageUrl
            ?: ""

    val displayType: String
        get() = type ?: "Unknown"

    val displayScore: String
        get() = score?.let { String.format(java.util.Locale.US, "%.1f", it) } ?: "N/A"
}

data class AnimeImages(
    @SerializedName("jpg") val jpg: ImageSizes? = null,
    @SerializedName("webp") val webp: ImageSizes? = null
)

data class ImageSizes(
    @SerializedName("image_url") val imageUrl: String? = null,
    @SerializedName("small_image_url") val smallImageUrl: String? = null,
    @SerializedName("large_image_url") val largeImageUrl: String? = null
)

data class Genre(
    @SerializedName("mal_id") val malId: Int,
    @SerializedName("name") val name: String
)

data class Pagination(
    @SerializedName("last_visible_page") val lastVisiblePage: Int? = null,
    @SerializedName("has_next_page") val hasNextPage: Boolean? = null
)
