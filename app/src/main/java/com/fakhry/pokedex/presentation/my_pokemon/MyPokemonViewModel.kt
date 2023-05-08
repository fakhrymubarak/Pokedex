package com.fakhry.pokedex.presentation.my_pokemon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.core.enums.UiState
import com.fakhry.pokedex.domain.model.MyPokemon
import com.fakhry.pokedex.domain.usecases.GetMyPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPokemonViewModel @Inject constructor(
    private val _getMyPokemon: GetMyPokemonUseCase
) : ViewModel() {

    private val _listMyPokemon = MutableSharedFlow<UiState<List<MyPokemon>>>()
    val listMyPokemon = _listMyPokemon.asSharedFlow()

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
}