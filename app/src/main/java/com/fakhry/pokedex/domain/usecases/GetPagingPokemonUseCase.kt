package com.fakhry.pokedex.domain.usecases

import com.fakhry.pokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPagingPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
}