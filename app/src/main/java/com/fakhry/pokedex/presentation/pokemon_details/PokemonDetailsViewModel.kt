package com.fakhry.pokedex.presentation.pokemon_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.core.enums.UiState
import com.fakhry.pokedex.core.enums.UiText
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.domain.usecases.CatchPokemonUseCase
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
    private val _getPokemonDetails: GetPokemonDetailsUseCase,
    private val _catchPokemon: CatchPokemonUseCase,
) : ViewModel() {

    private var _totalImages = 0
    private var _currImageIndex = 0
    private var _runCarouselJob: Job? = null
    val carouselState = MutableSharedFlow<Int>()

    private lateinit var pokemon: Pokemon
    private val _pokemonDetailsState = MutableSharedFlow<UiState<Pokemon>>()
    val pokemonDetailsState = _pokemonDetailsState.asSharedFlow()

    private val _catchPokemonState = MutableSharedFlow<UiState<UiText>>()
    val catchPokemonState = _catchPokemonState.asSharedFlow()

    private val _savePokemonState = MutableSharedFlow<UiState<UiText>>()
    val savePokemonState = _savePokemonState.asSharedFlow()

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
                is DataResource.Success -> {
                    pokemon = res.data
                    _pokemonDetailsState.emit(UiState.Success(pokemon))
                }
            }
            _pokemonDetailsState.emit(UiState.Loading(false))
        }
    }

    fun catchPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            _catchPokemonState.emit(UiState.Loading(true))
            when (val res = _catchPokemon()) {
                is DataResource.Error -> _catchPokemonState.emit(UiState.Error(res.uiText))
                is DataResource.Success -> _catchPokemonState.emit(UiState.Success(res.data))
            }
            _catchPokemonState.emit(UiState.Loading(false))
        }
    }

    fun saveMyPokemon(nickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _savePokemonState.emit(UiState.Loading(true))
            when (val res = _catchPokemon.saveMyPokemon(nickname, pokemon.id)) {
                is DataResource.Error -> _savePokemonState.emit(UiState.Error(res.uiText))
                is DataResource.Success -> _savePokemonState.emit(UiState.Success(res.data))
            }
            _savePokemonState.emit(UiState.Loading(false))
        }
    }
}