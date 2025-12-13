package com.gorman.pexelsappkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform