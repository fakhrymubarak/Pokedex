package com.fakhry.pokedex.domain.usecases

import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.domain.model.MyPokemon
import com.fakhry.pokedex.domain.model.mapToEntity
import com.fakhry.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class ReleaseMyPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(myPokemon: MyPokemon): DataResource<MyPokemon> {
        return when(val res = repository.releaseMyPokemon(myPokemon.mapToEntity())) {
            is DataResource.Error -> DataResource.Error(res.uiText)
            is DataResource.Success -> DataResource.Success(myPokemon)
        }
    }
}
