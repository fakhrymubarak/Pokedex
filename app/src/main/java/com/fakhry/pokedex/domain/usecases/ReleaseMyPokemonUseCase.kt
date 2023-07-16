package com.fakhry.pokedex.domain.usecases

import com.fakhry.pokedex.state.UiResult
import com.fakhry.pokedex.state.HttpClientResult
import com.fakhry.pokedex.domain.model.MyPokemon
import com.fakhry.pokedex.domain.model.mapToEntity
import com.fakhry.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class ReleaseMyPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(myPokemon: MyPokemon): UiResult<MyPokemon> {
        return when(repository.releaseMyPokemon(myPokemon.mapToEntity())) {
            is HttpClientResult.Failure -> UiResult.Error()
            is HttpClientResult.Success -> UiResult.Success(myPokemon)
        }
    }
}
