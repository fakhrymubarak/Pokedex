package com.fakhry.pokedex.presentation.my_pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.core.enums.UiState
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

    private val _listMyPokemon = MutableSharedFlow<UiState<List<MyPokemon>>>()
    val listMyPokemon = _listMyPokemon.asSharedFlow()

    private val _releasePokemonState = MutableSharedFlow<UiState<MyPokemon>>()
    val releasePokemonState = _releasePokemonState.asSharedFlow()

    fun getListPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            _listMyPokemon.emit(UiState.Loading(true))
            when (val res = _getMyPokemon()) {
                is DataResource.Error -> _listMyPokemon.emit(UiState.Error(res.uiText))
                is DataResource.Success -> _listMyPokemon.emit(UiState.Success(res.data))
            }
            _listMyPokemon.emit(UiState.Loading(false))
        }
    }

    fun releasePokemon(data: MyPokemon) {

        viewModelScope.launch(Dispatchers.IO) {
            _releasePokemonState.emit(UiState.Loading(true))
            when (val res = _releaseMyPokemon(data)) {
                is DataResource.Error -> _releasePokemonState.emit(UiState.Error(res.uiText))
                is DataResource.Success -> _releasePokemonState.emit(UiState.Success(res.data))
            }
            _releasePokemonState.emit(UiState.Loading(false))
        }
    }
}