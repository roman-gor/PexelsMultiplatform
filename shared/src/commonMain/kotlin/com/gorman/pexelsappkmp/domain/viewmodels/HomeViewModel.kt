package com.gorman.pexelsappkmp.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorman.pexelsappkmp.domain.states.HomeUiState
import com.gorman.pexelsappkmp.domain.states.PhotoLoadState
import com.gorman.pexelsappkmp.domain.models.ErrorID
import com.gorman.pexelsappkmp.domain.usecases.GetCuratedPhotosUseCase
import com.gorman.pexelsappkmp.domain.usecases.GetFeaturedCollectionsUseCase
import com.gorman.pexelsappkmp.domain.usecases.GetPhotosByQueryUseCase
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

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
        logger("HomeViewModel Initialized")
        loadFeaturedCollections()
        onSearch()
    }

    fun onSearch(query: String? = null) {
        logger("onSearch called with query: $query")
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
            logger("Starting search coroutine for query: $query")
            runCatching {
                val photosResult = if (query.isNullOrBlank()) {
                    getCuratedPhotosUseCase(page = currentPage)
                } else {
                    getPhotosByQueryUseCase(query, page = currentPage)
                }
                photosResult.onSuccess { photos ->
                    logger("Search successful. Photos found: ${photos.size}")
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
                    logger("Search FAILED: Type: ${throwable::class.simpleName}, Message: ${throwable.message}")
                    throwable.printStackTrace()
                    val error = when (throwable) {
                        is IOException -> ErrorID.NETWORK_UNAVAILABLE
                        is HttpRequestTimeoutException -> ErrorID.REQUEST_TIMEOUT

                        is ClientRequestException -> {
                            when (throwable.response.status.value) {
                                404 -> ErrorID.CLIENT_ERROR_404
                                401 -> ErrorID.CLIENT_ERROR_401
                                else -> ErrorID.UNKNOWN_ERROR
                            }
                        }
                        is ServerResponseException -> ErrorID.SERVER_ERROR_5XX
                        is SerializationException -> ErrorID.DATA_CORRUPTION
                        else -> ErrorID.UNKNOWN_ERROR
                    }
                    _uiState.update { it.copy(loadState = PhotoLoadState.Error(throwable, error)) }
                }
            }
        }
    }

    fun loadMore() {
        if (_uiState.value.loadState is PhotoLoadState.Loading || searchJob?.isActive == true) {
            logger("loadMore skipped: already loading or search job is active.")
            return
        }
        logger("loadMore called.")

        searchJob = viewModelScope.launch {
            _uiState.update { it.copy(loadState = PhotoLoadState.Loading) }
            logger("Starting loadMore coroutine. Page: $currentPage")
            runCatching {
                val currentQuery = _uiState.value.currentQuery
                val newPhotosResult = if (currentQuery.isNullOrBlank()) {
                    getCuratedPhotosUseCase(page = currentPage)
                } else {
                    getPhotosByQueryUseCase(currentQuery, page = currentPage)
                }
                newPhotosResult.onSuccess { newPhotos->
                    logger("Load more successful. Found ${newPhotos.size} new photos.")
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
                    logger("Search FAILED: Type: ${throwable::class.simpleName}, Message: ${throwable.message}")
                    throwable.printStackTrace()
                    val error = when (throwable) {
                        is IOException -> ErrorID.NETWORK_UNAVAILABLE
                        is HttpRequestTimeoutException -> ErrorID.REQUEST_TIMEOUT

                        is ClientRequestException -> {
                            when (throwable.response.status.value) {
                                404 -> ErrorID.CLIENT_ERROR_404
                                401 -> ErrorID.CLIENT_ERROR_401
                                else -> ErrorID.UNKNOWN_ERROR
                            }
                        }
                        is ServerResponseException -> ErrorID.SERVER_ERROR_5XX
                        is SerializationException -> ErrorID.DATA_CORRUPTION
                        else -> ErrorID.UNKNOWN_ERROR
                    }
                    _uiState.update { it.copy(loadState = PhotoLoadState.Error(throwable, error)) }
                }
            }
        }
    }

    private fun loadFeaturedCollections() {
        logger("loadFeaturedCollections called.")
        viewModelScope.launch {
            runCatching {
                logger("Fetching featured collections.")
                getFeaturedCollectionsUseCase()
            }.onSuccess { collections ->
                logger("Featured collections loaded successfully. Count: ${collections.size}")
                _uiState.update { it.copy(collections = collections) }
            }.onFailure { throwable ->
                logger("Failed to load featured collections: ${throwable.message}")
            }
        }
    }

    fun onCollectionSelected(title: String) {
        if (title == _uiState.value.selectedCollectionTitle) {
            onSearch()
            _uiState.update { it.copy(selectedCollectionTitle = null) }
        } else {
            onSearch(title)
        }
    }
}

expect fun logger(msg: String)
