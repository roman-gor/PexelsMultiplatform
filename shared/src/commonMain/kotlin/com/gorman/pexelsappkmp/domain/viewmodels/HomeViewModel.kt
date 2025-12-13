package com.gorman.pexelsappkmp.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorman.pexelsappkmp.domain.HomeUiState
import com.gorman.pexelsappkmp.domain.PhotoLoadState
import com.gorman.pexelsappkmp.domain.usecases.GetCuratedPhotosUseCase
import com.gorman.pexelsappkmp.domain.usecases.GetFeaturedCollectionsUseCase
import com.gorman.pexelsappkmp.domain.usecases.GetPhotosByQueryUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getCuratedPhotosUseCase: GetCuratedPhotosUseCase,
    private val getFeaturedCollectionsUseCase: GetFeaturedCollectionsUseCase,
    private val getPhotosByQueryUseCase: GetPhotosByQueryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private var currentPage = 1
    private var searchJob: Job? = null

    init {
        loadFeaturedCollections()
        onSearch()
    }

    fun onSearch(query: String? = null) {
        searchJob?.cancel()
        currentPage = 1
        _uiState.update {
            it.copy(
                photos = emptyList(),
                currentQuery = query,
                loadState = PhotoLoadState.Loading,
                noResults = false,
                selectedCollectionTitle = query
            )
        }

        searchJob = viewModelScope.launch {
            runCatching {
                val photos = if (query.isNullOrBlank()) {
                    getCuratedPhotosUseCase(page = currentPage)
                } else {
                    getPhotosByQueryUseCase(query, page = currentPage)
                }
                _uiState.update {
                    it.copy(
                        photos = photos,
                        loadState = PhotoLoadState.Idle,
                        noResults = photos.isEmpty()
                    )
                }
                if (photos.isNotEmpty()) {
                    currentPage++
                }
            }.onFailure { throwable ->
                _uiState.update { it.copy(loadState = PhotoLoadState.Error(throwable)) }
            }
        }
    }

    fun loadMore() {
        if (_uiState.value.loadState is PhotoLoadState.Loading || searchJob?.isActive == true) {
            return
        }

        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(loadState = PhotoLoadState.Loading) }
            runCatching {
                val currentQuery = _uiState.value.currentQuery
                val newPhotos = if (currentQuery.isNullOrBlank()) {
                    getCuratedPhotosUseCase(page = currentPage)
                } else {
                    getPhotosByQueryUseCase(currentQuery, page = currentPage)
                }
                _uiState.update {
                    it.copy(
                        photos = it.photos + newPhotos,
                        loadState = PhotoLoadState.Idle
                    )
                }
                if (newPhotos.isNotEmpty()) {
                    currentPage++
                }
            }.onFailure { throwable ->
                _uiState.update { it.copy(loadState = PhotoLoadState.Error(throwable)) }
            }
        }
    }

    private fun loadFeaturedCollections() {
        viewModelScope.launch {
            runCatching {
                getFeaturedCollectionsUseCase()
            }.onSuccess { collections ->
                _uiState.update { it.copy(collections = collections) }
            }.onFailure {
                // Log error, maybe update UI to show error for collections
            }
        }
    }

    fun onCollectionSelected(title: String) {
        if (title == _uiState.value.selectedCollectionTitle) {
            onSearch() // Go back to curated
            _uiState.update { it.copy(selectedCollectionTitle = null) }
        } else {
            onSearch(title)
        }
    }
}
