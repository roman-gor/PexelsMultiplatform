package com.gorman.pexelsappkmp.domain.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gorman.pexelsappkmp.domain.models.Bookmark
import com.gorman.pexelsappkmp.domain.usecases.GetAllBookmarksUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookmarksViewModel (
    private val getAllBookmarksUseCases: GetAllBookmarksUseCase
) : ViewModel() {
    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())
    val bookmarks: StateFlow<List<Bookmark>> = _bookmarks.asStateFlow()

    private val _bookmarksProgress = MutableStateFlow(0)
    val bookmarksProgress = _bookmarksProgress.asStateFlow()

    fun loadBookmarks() {
        viewModelScope.launch {
            try {
                _bookmarksProgress.value = 0
                getAllBookmarksUseCases().collect { list ->
                    val total = list.size
                    if (total == 0)
                    {
                        _bookmarks.value = emptyList()
                        _bookmarksProgress.value = 100
                        return@collect
                    }
                    val loaded = mutableListOf<Bookmark>()

                    for ((index, bookmark) in list.withIndex()){
                        delay(30L)
                        loaded.add(bookmark)
                        _bookmarks.value = loaded.toList()
                        _bookmarksProgress.value = ((index + 1) * 100 / total)
                    }
                }
            } catch (e: Exception) {
                //Log.e("BookmarksVM", "Ошибка при загрузке: ${e.message}", e)
            } finally {
                _bookmarksProgress.value = 100
            }
        }
    }
}