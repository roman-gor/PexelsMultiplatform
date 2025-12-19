package com.gorman.pexelsappkmp.data.mappers

import com.gorman.pexelsappkmp.data.models.CollectionsResponse
import com.gorman.pexelsappkmp.domain.models.Collection

fun CollectionsResponse.toDomain(): List<Collection> = collections.map { Collection(
    id = it.id,
    title = it.title,
    description = it.description,
    isPrivate = it.isPrivate,
    mediaCount = it.mediaCount,
    photosCount = it.photosCount,
    videosCount = it.videosCount)
}