package com.gorman.pexelsappkmp.data.mappers

import com.gorman.pexelsappkmp.data.models.CollectionsResponse
import com.gorman.pexelsappkmp.domain.models.Collection

fun CollectionsResponse.toDomain(): List<Collection> = collections.map { Collection(
    id = it.id,
    title = it.title,
    description = it.description,
    private = it.private,
    mediaCount = it.media_count,
    photosCount = it.photos_count,
    videosCount = it.videos_count)
}