package com.fakhry.pokedex.data.source.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fakhry.pokedex.core.enums.NetworkException
import com.fakhry.pokedex.core.network.NetworkState
import com.fakhry.pokedex.data.model.PokemonData
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class PokemonPagingSource(
    private val networkState: NetworkState,
    private val apiService: PokeApiService,
) : PagingSource<Int, PokemonData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonData> {
        return try {
            if (networkState.isNetworkNotAvailable) throw NetworkException()

            val nextOffset = params.key ?: 0
            val result = apiService

            val apiResult = result.getPokemonList(nextOffset, POKEMON_PAGING_LIMIT)
            val listPokemon = apiResult.results

            if (listPokemon.isEmpty()) {
                return LoadResult.Page(listOf(), null, null)
            }

            LoadResult.Page(
                data = listPokemon,
                prevKey = null, // Only paging forward.
                nextKey = if (nextOffset > apiResult.count) null else nextOffset + POKEMON_PAGING_LIMIT
            )
        } catch (e: IOException) {
            Timber.e(e)
            LoadResult.Error(e)
        } catch (e: HttpException) {
            Timber.e(e)
            LoadResult.Error(e)
        } catch (e: NetworkException) {
            Timber.e(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    companion object {
        const val POKEMON_PAGING_LIMIT = 10
    }
}