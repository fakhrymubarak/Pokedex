package com.fakhry.pokedex.data.repository

import com.fakhry.pokedex.core.network.NetworkState
import com.fakhry.pokedex.data.source.remote.PokeApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val networkState: NetworkState,
    private val apiService: PokeApiService,
) {
}