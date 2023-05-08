package com.fakhry.pokedex.presentation.pokemon_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.core.enums.UiState
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.domain.usecases.GetPokemonDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val _getPokemonDetails: GetPokemonDetailsUseCase
) : ViewModel() {

    private var _totalImages = 0
    private var _currImageIndex = 0
    private var _runCarouselJob: Job? = null
    val carouselState = MutableSharedFlow<Int>()

    private val _pokemonDetailsState = MutableSharedFlow<UiState<Pokemon>>()
    val pokemonDetailsState = _pokemonDetailsState.asSharedFlow()

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

    fun getPokemonDetails(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _pokemonDetailsState.emit(UiState.Loading(true))
            when (val res = _getPokemonDetails(id)) {
                is DataResource.Error -> _pokemonDetailsState.emit(UiState.Error(res.uiText))
                is DataResource.Success -> _pokemonDetailsState.emit(UiState.Success(res.data))
            }
            _pokemonDetailsState.emit(UiState.Loading(false))
        }
    }
}