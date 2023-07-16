package com.fakhry.pokedex.state

import com.fakhry.pokedex.core.enums.UiText

sealed class UiResult<out T> {
    data class Loading(val isLoading: Boolean) : UiResult<Nothing>()
    data class Success<out T>(val data: T) : UiResult<T>()
    data class Error(val uiText: UiText = UiText.unknownError) : UiResult<Nothing>()
}