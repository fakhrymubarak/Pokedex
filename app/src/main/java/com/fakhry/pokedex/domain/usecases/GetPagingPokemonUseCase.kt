package com.fakhry.pokedex.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.data.model.mapToDomain
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
            pagingData.map { data ->
                val listPath = data.url.split('/').toMutableList()
                listPath.removeLast()
                val id = listPath.last().toInt()

                when (val detailsResult = repository.getPokemonDetails(id)) {
                    is DataResource.Error -> data.mapToDomain()
                    is DataResource.Success -> detailsResult.data.mapToDomain()
                }
            }
        }
    }
}