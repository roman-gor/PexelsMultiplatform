package com.gorman.pexelsappkmp.domain.states

import com.gorman.pexelsappkmp.domain.models.Collection
import com.gorman.pexelsappkmp.domain.models.ErrorID
import com.gorman.pexelsappkmp.domain.models.Photo

data class HomeUiState(
    val photos: List<Photo> = emptyList(),
    val collections: List<Collection> = emptyList(),
    val loadState: PhotoLoadState = PhotoLoadState.Idle,
    val selectedCollectionTitle: String? = null,
    val currentQuery: String? = null,
    val noResults: Boolean = false,
)

sealed interface PhotoLoadState {
    object Idle : PhotoLoadState
    object Loading : PhotoLoadState
    data class Error(val error: Throwable, val errorId: ErrorID) : PhotoLoadState
}
