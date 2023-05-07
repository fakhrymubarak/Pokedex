package com.fakhry.pokedex.presentation.pokemon_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
) : ViewModel() {

    private var _totalImages = 0
    private var _currImageIndex = 0
    private var _runCarouselJob: Job? = null
    val carouselState = MutableSharedFlow<Int>()

    override fun onCleared() {
        _runCarouselJob?.cancel()
        super.onCleared()
    }

    fun setTotalPictures(total: Int) {
        _totalImages = total
    }

    fun runCarousel() {
        _runCarouselJob = viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            val nextIndex = if (_currImageIndex >= _totalImages - 1) 0 else (_currImageIndex + 1)
            carouselState.emit(nextIndex)
        }
    }

    fun setCarouselCurrentIndex(index: Int) {
        _runCarouselJob?.cancel()
        _currImageIndex = index
        runCarousel()
    }
}