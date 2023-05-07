package com.fakhry.pokedex.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.fakhry.pokedex.core.utils.components.collectLifecycleFlow
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.domain.usecases.GetPagingPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getPagingPokemon: GetPagingPokemonUseCase
) : ViewModel() {
    private val _listBusiness = MutableStateFlow<PagingData<Pokemon>?>(null)
    val listBusiness = _listBusiness.asStateFlow()

    suspend fun getListBusiness() {
        collectLifecycleFlow(getPagingPokemon()) { _listBusiness.emit(it) }
    }

    fun loadDetails() {
        // fetch details to get pokemon images.
    }
}