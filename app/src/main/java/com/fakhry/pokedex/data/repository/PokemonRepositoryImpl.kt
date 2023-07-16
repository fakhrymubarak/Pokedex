package com.fakhry.pokedex.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.fakhry.pokedex.core.enums.DatabaseError
import com.fakhry.pokedex.state.HttpClientResult
import com.fakhry.pokedex.core.network.NetworkState
import com.fakhry.pokedex.data.http.PokeApiService
import com.fakhry.pokedex.data.http.PokemonPagingSource
import com.fakhry.pokedex.data.http.response.PokemonData
import com.fakhry.pokedex.data.http.response.PokemonDetailsResponse
import com.fakhry.pokedex.data.infrastructure.execute
import com.fakhry.pokedex.data.room.PokemonDao
import com.fakhry.pokedex.data.room.entity.MyPokemonEntity
import com.fakhry.pokedex.data.room.entity.PokemonEntity
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

    override suspend fun getPokemonDetails(id: Int): HttpClientResult<PokemonDetailsResponse> = execute { apiService.getPokemonDetails(id) }


    override suspend fun insertPokemon(pokemon: PokemonEntity): HttpClientResult<Boolean> {
        return try {
            dao.insertPokemon(pokemon)
            HttpClientResult.Success(true)
        } catch (e: Exception) {
            Timber.e(e)
            HttpClientResult.Failure(DatabaseError())
        }
    }

    override suspend fun getPokemonLocally(id: Int): HttpClientResult<PokemonEntity> {
        return try {
            val localResult = dao.getPokemon(id = id) ?: throw Exception("Pokemon Not Found")
            HttpClientResult.Success(localResult)
        } catch (e: Exception) {
            Timber.e(e)
            HttpClientResult.Failure(DatabaseError())
        }
    }


    override suspend fun getMyPokemons(): HttpClientResult<List<MyPokemonEntity>> {
        return try {
            val localResult = dao.getMyPokemon()
            HttpClientResult.Success(localResult)
        } catch (e: Exception) {
            Timber.e(e)
            HttpClientResult.Failure(DatabaseError())
        }
    }

    override suspend fun releaseMyPokemon(myPokemonEntity: MyPokemonEntity): HttpClientResult<MyPokemonEntity> {
        return try {
            dao.deleteMyPokemon(myPokemonEntity)
            HttpClientResult.Success(myPokemonEntity)
        } catch (e: Exception) {
            Timber.e(e)
            HttpClientResult.Failure(DatabaseError())
        }    }

    override suspend fun insertMyPokemon(nickname: String, pokemonId: Int): HttpClientResult<Boolean> {
        return try {
            dao.insertMyPokemon(MyPokemonEntity(nickname = nickname, pokemonId = pokemonId))
            HttpClientResult.Success(true)
        } catch (e: Exception) {
            Timber.e(e)
            HttpClientResult.Failure(DatabaseError())
        }
    }
}