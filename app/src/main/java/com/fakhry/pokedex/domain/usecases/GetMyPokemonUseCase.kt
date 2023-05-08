package com.fakhry.pokedex.domain.usecases

import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.data.source.local.entity.mapToDomain
import com.fakhry.pokedex.domain.model.MyPokemon
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetMyPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): DataResource<List<MyPokemon>> {
        return when (val res = repository.getMyPokemons()) {
            is DataResource.Error -> DataResource.Error(res.uiText)
            is DataResource.Success -> {
                val myPokemon = res.data.map { entity ->
                    val pokemon = when (val resPokemon = repository.getPokemonLocally(entity.pokemonId)) {
                            is DataResource.Error -> Pokemon(id = entity.pokemonId, name = "-")
                            is DataResource.Success -> resPokemon.data.mapToDomain()
                        }
                    entity.mapToDomain(pokemon)
                }
                DataResource.Success(myPokemon)
            }
        }
    }
}
