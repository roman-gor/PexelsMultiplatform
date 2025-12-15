package com.gorman.pexelsappkmp.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorman.pexelsappkmp.domain.models.Bookmark
import com.gorman.pexelsappkmp.domain.usecases.AddBookmarkUseCase
import com.gorman.pexelsappkmp.domain.usecases.DeleteBookmarkByUrlUseCase
import com.gorman.pexelsappkmp.domain.usecases.FindBookmarkByIdUseCase
import com.gorman.pexelsappkmp.domain.usecases.SearchInDBOnceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel (
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val searchInDBOnceUseCase: SearchInDBOnceUseCase,
    private val deleteBookmarkByUrlUseCase: DeleteBookmarkByUrlUseCase,
    private val findBookmarkByIdUseCase: FindBookmarkByIdUseCase
) : ViewModel() {

    private val _bookmark = MutableStateFlow<Bookmark?>(null)
    val bookmark: StateFlow<Bookmark?> = _bookmark.asStateFlow()

    fun addBookmark(imageUrl: String, name: String) {
        viewModelScope.launch {
            addBookmarkUseCase(imageUrl, name)
        }
    }

    fun findBookmarkById(imageId: Int) {
        viewModelScope.launch {
            _bookmark.value = findBookmarkByIdUseCase(imageId)
        }
    }

    fun searchInDBOnce(url: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = searchInDBOnceUseCase(url)
            onResult(result)
        }
    }

    fun deleteByUrl(url: String) {
        viewModelScope.launch {
            deleteBookmarkByUrlUseCase(url)
        }
    }
}