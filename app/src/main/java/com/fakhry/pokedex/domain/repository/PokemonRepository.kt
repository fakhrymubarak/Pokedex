package com.fakhry.pokedex.domain.repository

import androidx.paging.Pager
import com.fakhry.pokedex.state.HttpClientResult
import com.fakhry.pokedex.data.http.response.PokemonData
import com.fakhry.pokedex.data.http.response.PokemonDetailsResponse
import com.fakhry.pokedex.data.room.entity.MyPokemonEntity
import com.fakhry.pokedex.data.room.entity.PokemonEntity

interface PokemonRepository {
    suspend fun getPagingPokemon(): Pager<Int, PokemonData>
    suspend fun getPokemonDetails(id: Int): HttpClientResult<PokemonDetailsResponse>

    suspend fun insertMyPokemon(nickname: String, pokemonId: Int): HttpClientResult<Boolean>
    suspend fun getMyPokemons(): HttpClientResult<List<MyPokemonEntity>>
    suspend fun releaseMyPokemon(myPokemonEntity: MyPokemonEntity): HttpClientResult<MyPokemonEntity>

    suspend fun insertPokemon(pokemon: PokemonEntity): HttpClientResult<Boolean>
    suspend fun getPokemonLocally(id: Int): HttpClientResult<PokemonEntity>

}