package com.fakhry.pokedex.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.core.enums.UiText
import com.fakhry.pokedex.core.network.NetworkState
import com.fakhry.pokedex.core.network.getMessageFromException
import com.fakhry.pokedex.data.source.remote.response.PokemonData
import com.fakhry.pokedex.data.source.remote.response.PokemonDetailsResponse
import com.fakhry.pokedex.data.source.local.entity.MyPokemonEntity
import com.fakhry.pokedex.data.source.local.entity.PokemonEntity
import com.fakhry.pokedex.data.source.local.room.PokemonDao
import com.fakhry.pokedex.data.source.remote.PokeApiService
import com.fakhry.pokedex.data.source.remote.PokemonPagingSource
import com.fakhry.pokedex.domain.repository.PokemonRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepositoryImpl @Inject constructor(
    private val networkState: NetworkState,
    private val apiService: PokeApiService,
    private val dao: PokemonDao,
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

    override suspend fun insertPokemon(pokemon: PokemonEntity): DataResource<Boolean> {
        return try {
            dao.insertPokemon(pokemon)
            DataResource.Success(true)
        } catch (e: Exception) {
            Timber.e(e)
            DataResource.Error(UiText.DynamicString(e.message ?: "Database Failure"))
        }
    }

    override suspend fun getPokemonLocally(id: Int): DataResource<PokemonEntity> {
        return try {
            val localResult = dao.getPokemon(id = id) ?: throw Exception("Pokemon Not Found")
            DataResource.Success(localResult)
        } catch (e: Exception) {
            Timber.e(e)
            DataResource.Error(UiText.DynamicString(e.message ?: "Database Failure"))
        }
    }


    override suspend fun getMyPokemons(): DataResource<List<MyPokemonEntity>> {
        return try {
            val localResult = dao.getMyPokemon()
            DataResource.Success(localResult)
        } catch (e: Exception) {
            Timber.e(e)
            DataResource.Error(UiText.DynamicString(e.message ?: "Database Failure"))
        }
    }

    override suspend fun insertMyPokemon(nickname: String, pokemonId: Int): DataResource<Boolean> {
        return try {
            dao.insertMyPokemon(MyPokemonEntity(nickname = nickname, pokemonId = pokemonId))
            DataResource.Success(true)
        } catch (e: Exception) {
            Timber.e(e)
            DataResource.Error(UiText.DynamicString(e.message ?: "Database Failure"))
        }
    }


}