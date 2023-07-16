package com.fakhry.pokedex.presentation.my_pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.pokedex.state.UiResult
import com.fakhry.pokedex.domain.model.MyPokemon
import com.fakhry.pokedex.domain.usecases.GetMyPokemonUseCase
import com.fakhry.pokedex.domain.usecases.ReleaseMyPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPokemonViewModel @Inject constructor(
    private val _getMyPokemon: GetMyPokemonUseCase,
    private val _releaseMyPokemon: ReleaseMyPokemonUseCase,
) : ViewModel() {

    private val _listMyPokemon = MutableSharedFlow<UiResult<List<MyPokemon>>>()
    val listMyPokemon = _listMyPokemon.asSharedFlow()

    private val _releasePokemonState = MutableSharedFlow<UiResult<MyPokemon>>()
    val releasePokemonState = _releasePokemonState.asSharedFlow()

    fun getListPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            _listMyPokemon.emit(UiResult.Loading(true))
            when (val res = _getMyPokemon()) {
                is UiResult.Error -> _listMyPokemon.emit(UiResult.Error())
                is UiResult.Success -> _listMyPokemon.emit(UiResult.Success(res.data))
                is UiResult.Loading -> _listMyPokemon.emit(UiResult.Loading(true))
            }
            _listMyPokemon.emit(UiResult.Loading(false))
        }
    }

    fun releasePokemon(data: MyPokemon) {

        viewModelScope.launch(Dispatchers.IO) {
            _releasePokemonState.emit(UiResult.Loading(true))
            when (val res = _releaseMyPokemon(data)) {
                is UiResult.Error -> _releasePokemonState.emit(UiResult.Error())
                is UiResult.Success -> _releasePokemonState.emit(UiResult.Success(res.data))
                is UiResult.Loading -> _listMyPokemon.emit(UiResult.Loading(true))
            }
            _releasePokemonState.emit(UiResult.Loading(false))
        }
    }
}