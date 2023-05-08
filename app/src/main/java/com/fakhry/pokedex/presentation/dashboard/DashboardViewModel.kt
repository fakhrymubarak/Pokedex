package com.fakhry.pokedex.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.domain.usecases.GetPagingPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPagingPokemon: GetPagingPokemonUseCase
) : ViewModel() {
    private val _listPokemon = MutableStateFlow<PagingData<Pokemon>?>(null)
    val listPokemon = _listPokemon.asStateFlow()

    fun getListPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            getPagingPokemon()
                .cachedIn(this)
                .collectLatest { _listPokemon.emit(it) }
        }
    }
}