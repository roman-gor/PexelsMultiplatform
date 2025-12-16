package com.gorman.pexelsappkmp.domain.models

enum class ErrorID {
    NETWORK_UNAVAILABLE,
    REQUEST_TIMEOUT,
    CLIENT_ERROR_404,
    CLIENT_ERROR_401,
    SERVER_ERROR_5XX,
    DATA_CORRUPTION,
    UNKNOWN_ERROR
}