package com.fakhry.pokedex.domain.repository

import androidx.paging.Pager
import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.data.source.local.entity.MyPokemonEntity
import com.fakhry.pokedex.data.source.local.entity.PokemonEntity
import com.fakhry.pokedex.data.source.remote.response.PokemonData
import com.fakhry.pokedex.data.source.remote.response.PokemonDetailsResponse

interface PokemonRepository {
    suspend fun getPagingPokemon(): Pager<Int, PokemonData>
    suspend fun getPokemonDetails(id: Int): DataResource<PokemonDetailsResponse>

    suspend fun insertMyPokemon(nickname: String, pokemonId: Int): DataResource<Boolean>
    suspend fun getMyPokemons(): DataResource<List<MyPokemonEntity>>
    suspend fun releaseMyPokemon(myPokemonEntity: MyPokemonEntity): DataResource<MyPokemonEntity>

    suspend fun insertPokemon(pokemon: PokemonEntity): DataResource<Boolean>
    suspend fun getPokemonLocally(id: Int): DataResource<PokemonEntity>

}