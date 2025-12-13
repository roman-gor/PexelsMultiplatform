package com.gorman.pexelsappkmp.data.mappers

import com.gorman.pexelsappkmp.data.models.PexelsResponse
import com.gorman.pexelsappkmp.data.models.SrcData
import com.gorman.pexelsappkmp.domain.models.Photo
import com.gorman.pexelsappkmp.domain.models.Src

fun PexelsResponse.toDomain(): List<Photo> = photos.map { Photo(
    it.id,
    it.url,
    it.photographer,
    it.photographer_url,
    it.photographer_id,
    it.src.toDomain())
}

fun SrcData.toDomain(): Src = Src(
    original = original,
    large2x = large2x,
    large = large,
    medium = medium,
    small = small,
    portrait = portrait,
    landscape = landscape,
    tiny = tiny
)