package com.gorman.pexelsappkmp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionsResponse(
    val page: Int = 0,
    @SerialName("per_page")
    val perPage: Int = 0,
    @SerialName("total_results")
    val totalResults: Int = 0,
    val collections: List<CollectionItem> = emptyList(),
    @SerialName("next_page")
    val nextPage: String? = null,
    @SerialName("prev_page")
    val prevPage: String? = null
)

@Serializable
data class CollectionItem(
    val id: String = "",
    val title: String = "",
    val description: String? = null,
    val private: Boolean = false,
    @SerialName("media_count")
    val mediaCount: Int = 0,
    @SerialName("photos_count")
    val photosCount: Int = 0,
    @SerialName("videos_count")
    val videosCount: Int = 0
)
