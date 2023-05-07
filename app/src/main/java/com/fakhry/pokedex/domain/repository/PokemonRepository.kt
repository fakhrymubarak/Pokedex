package com.fakhry.pokedex.domain.repository

import androidx.paging.Pager
import com.fakhry.pokedex.data.model.PokemonData

interface PokemonRepository {
    suspend fun getPagingPokemon(): Pager<Int, PokemonData>
}