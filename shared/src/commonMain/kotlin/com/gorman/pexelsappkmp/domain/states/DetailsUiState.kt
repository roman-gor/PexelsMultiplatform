package com.gorman.pexelsappkmp.domain.states

sealed class DetailsUiState {
    object Loading: DetailsUiState()
    object Idle: DetailsUiState()
    data class Success(val isBookmark: Boolean): DetailsUiState()
    data class Error(val e: Throwable): DetailsUiState()
}