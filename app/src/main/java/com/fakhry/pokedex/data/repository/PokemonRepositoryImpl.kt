package com.fakhry.pokedex.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.core.enums.UiText
import com.fakhry.pokedex.core.network.NetworkState
import com.fakhry.pokedex.core.network.getMessageFromException
import com.fakhry.pokedex.data.model.PokemonData
import com.fakhry.pokedex.data.model.PokemonDetailsResponse
import com.fakhry.pokedex.data.source.remote.PokeApiService
import com.fakhry.pokedex.data.source.remote.PokemonPagingSource
import com.fakhry.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val networkState: NetworkState,
    private val apiService: PokeApiService,
) : PokemonRepository {

    override suspend fun getPagingPokemon(): Pager<Int, PokemonData> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = 10
            ),
            pagingSourceFactory = {
                PokemonPagingSource(
                    networkState = networkState,
                    apiService = apiService,
                )
            }
        )
    }

    override suspend fun getPokemonDetails(id: Int): DataResource<PokemonDetailsResponse> {
        if (networkState.isNetworkNotAvailable) return DataResource.Error(UiText.networkError)

        return try {
            val result = apiService.getPokemonDetails(id)
            DataResource.Success(result)
        } catch (e: Exception) {
            val networkException = getMessageFromException(e)
            DataResource.Error(networkException.errorMessages)
        }
    }
}