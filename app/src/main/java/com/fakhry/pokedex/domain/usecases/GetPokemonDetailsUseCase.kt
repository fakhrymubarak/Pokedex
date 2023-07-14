package com.fakhry.pokedex.domain.usecases

import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.data.http.response.mapToDomain
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonDetailsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {

    suspend operator fun invoke(id: Int): DataResource<Pokemon> {
        return when (val dataResult = repository.getPokemonDetails(id)) {
            is DataResource.Error -> dataResult
            is DataResource.Success -> DataResource.Success(dataResult.data.mapToDomain())
        }
    }
}