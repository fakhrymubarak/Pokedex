package com.fakhry.pokedex.data.http

import com.fakhry.pokedex.data.http.response.PokemonDetailsResponse
import com.fakhry.pokedex.data.http.response.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 10,
    ): PokemonResponse

    @GET("pokemon/{id}/")
    suspend fun getPokemonDetails(
        @Path("id") id: Int,
    ): PokemonDetailsResponse

}
