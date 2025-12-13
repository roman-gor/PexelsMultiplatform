package com.gorman.pexelsappkmp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PexelsResponse(
    val total_results: Int,
    val page: Int,
    val per_page: Int,
    val photos: List<PhotoData>,
    val next_page: String?
)

@Serializable
data class PhotoData(
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographer_url: String,
    val photographer_id: Long,
    val avg_color: String,
    val src: SrcData,
    val liked: Boolean,
    val alt: String
)

@Serializable
data class SrcData(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)
