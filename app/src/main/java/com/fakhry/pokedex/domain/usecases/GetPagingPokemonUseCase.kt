package com.fakhry.pokedex.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.data.source.remote.response.mapToDomain
import com.fakhry.pokedex.data.source.remote.response.mapToEntity
import com.fakhry.pokedex.data.source.local.entity.mapToDomain
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPagingPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): Flow<PagingData<Pokemon>> {
        return repository.getPagingPokemon().flow.map { pagingData ->

            // map data to domain
            pagingData.map mapper@{ data ->
                val listPath = data.url.split('/').toMutableList()
                listPath.removeLast()
                val pokemonId = listPath.last().toInt()

                // get pokemon details from localDB
                when (val localPokemon = repository.getPokemonLocally(pokemonId)) {
                    is DataResource.Error -> {}
                    is DataResource.Success -> return@mapper localPokemon.data.mapToDomain()
                }

                // get pokemon details from Network
                when (val detailsResult = repository.getPokemonDetails(pokemonId)) {
                    is DataResource.Error -> data.mapToDomain()
                    is DataResource.Success -> {
                        repository.insertPokemon(detailsResult.data.mapToEntity())
                        detailsResult.data.mapToDomain()
                    }
                }
            }
        }
    }
}