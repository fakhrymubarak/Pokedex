package com.fakhry.pokedex.domain.usecases

import androidx.paging.PagingData
import androidx.paging.map
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
            pagingData.map { data -> data.mapToDomain() }
        }
    }
}