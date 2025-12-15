package com.gorman.pexelsappkmp.domain.viewmodels

import android.util.Log

actual fun logger(msg: String) {
    Log.e("APP", msg)
}