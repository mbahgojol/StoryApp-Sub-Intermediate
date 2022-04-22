package com.mbahgojol.storyapp.utils

sealed class ResultState {
    data class Loading(val loading: Boolean) : ResultState()
    data class Success<T>(val data: T) : ResultState()
    data class Error(val e: Throwable) : ResultState()
}