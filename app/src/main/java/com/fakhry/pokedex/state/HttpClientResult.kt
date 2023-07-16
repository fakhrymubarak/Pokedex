package com.fakhry.pokedex.state

sealed class HttpClientResult<out T> {
    data class Success<out T>(val data: T) : HttpClientResult<T>()
    data class Failure(val throwable: Throwable) : HttpClientResult<Nothing>()
}