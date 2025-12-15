package com.gorman.pexelsappkmp.data.mappers

import com.gorman.pexelsappkmp.data.models.BookmarkImage
import com.gorman.pexelsappkmp.domain.models.Bookmark

fun BookmarkImage.toDomain(): Bookmark = Bookmark(
    id = id,
    imageUrl = imageUrl,
    phName = phName
)