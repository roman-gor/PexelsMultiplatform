package com.gorman.pexelsappkmp.domain.viewmodels

import androidx.lifecycle.ViewModel

interface KmpViewModel {
    fun clear()
}

open class BaseViewModel : ViewModel(), KmpViewModel {
    override fun clear() { onCleared() }
}