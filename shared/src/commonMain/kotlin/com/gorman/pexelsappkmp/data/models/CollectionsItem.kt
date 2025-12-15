package com.gorman.pexelsappkmp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CollectionsResponse(
    val page: Int,
    val per_page: Int,
    val total_results: Int,
    val collections: List<CollectionItem>,
    val next_page: String? = null,
    val prev_page: String? = null
)

@Serializable
data class CollectionItem(
    val id: String,
    val title: String,
    val description: String? = null,
    val private: Boolean,
    val media_count: Int,
    val photos_count: Int,
    val videos_count: Int
)
