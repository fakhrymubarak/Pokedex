package com.fakhry.pokedex.domain.repository

import androidx.paging.Pager
import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.data.model.PokemonData
import com.fakhry.pokedex.data.model.PokemonDetailsResponse

interface PokemonRepository {
    suspend fun getPagingPokemon(): Pager<Int, PokemonData>
    suspend fun getPokemonDetails(id: Int): DataResource<PokemonDetailsResponse>
}