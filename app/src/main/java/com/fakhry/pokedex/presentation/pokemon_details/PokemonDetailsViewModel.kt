package com.fakhry.pokedex.presentation.pokemon_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.pokedex.state.HttpClientResult
import com.fakhry.pokedex.state.UiResult
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
    private val _pokemonDetailsState = MutableSharedFlow<UiResult<Pokemon>>()
    val pokemonDetailsState = _pokemonDetailsState.asSharedFlow()

    private val _catchPokemonState = MutableSharedFlow<UiResult<UiText>>()
    val catchPokemonState = _catchPokemonState.asSharedFlow()

    private val _savePokemonState = MutableSharedFlow<UiResult<UiText>>()
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
            _pokemonDetailsState.emit(UiResult.Loading(true))
            when (val res = _getPokemonDetails(id)) {
                is HttpClientResult.Failure -> _pokemonDetailsState.emit(UiResult.Error())
                is HttpClientResult.Success -> {
                    pokemon = res.data
                    _pokemonDetailsState.emit(UiResult.Success(pokemon))
                }
            }
            _pokemonDetailsState.emit(UiResult.Loading(false))
        }
    }

    fun catchPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            _catchPokemonState.emit(UiResult.Loading(true))
            when (val res = _catchPokemon()) {
                is UiResult.Error -> _catchPokemonState.emit(UiResult.Error())
                is UiResult.Success -> _catchPokemonState.emit(UiResult.Success(res.data))
                is UiResult.Loading -> _catchPokemonState.emit(UiResult.Loading(true))
            }
            _catchPokemonState.emit(UiResult.Loading(false))
        }
    }

    fun saveMyPokemon(nickname: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _savePokemonState.emit(UiResult.Loading(true))
            when (val res = _catchPokemon.saveMyPokemon(nickname, pokemon.id)) {
                is UiResult.Error -> _savePokemonState.emit(UiResult.Error())
                is UiResult.Success -> _savePokemonState.emit(UiResult.Success(res.data))
                is UiResult.Loading -> _savePokemonState.emit(UiResult.Loading(true))
            }
            _savePokemonState.emit(UiResult.Loading(false))
        }
    }
}