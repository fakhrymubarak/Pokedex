package com.fakhry.pokedex.domain.usecases

import com.fakhry.pokedex.state.UiResult
import com.fakhry.pokedex.state.HttpClientResult
import com.fakhry.pokedex.data.room.entity.mapToDomain
import com.fakhry.pokedex.domain.model.MyPokemon
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetMyPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): UiResult<List<MyPokemon>> {
        return when (val res = repository.getMyPokemons()) {
            is HttpClientResult.Failure -> UiResult.Error()
            is HttpClientResult.Success -> {
                val myPokemon = res.data.map { entity ->
                    val pokemon = when (val resPokemon = repository.getPokemonLocally(entity.pokemonId)) {
                            is HttpClientResult.Failure -> Pokemon(id = entity.pokemonId, name = "-")
                            is HttpClientResult.Success -> resPokemon.data.mapToDomain()
                        }
                    entity.mapToDomain(pokemon)
                }
                UiResult.Success(myPokemon)
            }
        }
    }
}
