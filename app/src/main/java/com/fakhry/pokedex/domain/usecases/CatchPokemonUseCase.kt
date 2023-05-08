package com.fakhry.pokedex.domain.usecases

import com.fakhry.pokedex.core.enums.DataResource
import com.fakhry.pokedex.core.enums.UiText
import com.fakhry.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class CatchPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): DataResource<UiText> {
        delay(1500)
        val isCaught = Math.random() > 0.5

        return if (isCaught) {
            val listOfSuccessMessage = listOf(
                "Gotcha! Pokémon was caught!",
                "All right! Pokémon was caught!",
            )
            DataResource.Success(UiText.DynamicString(listOfSuccessMessage.random()))
        } else {
            val listOfFailedMessage = listOf(
                "Oh no! The Pokémon broke free! Try again!",
                "Aargh! Almost had it! Try Again!",
            )
            DataResource.Error(UiText.DynamicString(listOfFailedMessage.random()))
        }
    }

    suspend fun saveMyPokemon(_nickname: String, pokemonId: Int): DataResource<UiText> {
        val nickname = _nickname.trim()

        if (nickname.isEmpty()) {
            val emptyTextError = UiText.DynamicString("Please fill the nickname of your Pokémon!")
            return DataResource.Error(emptyTextError)
        }

        return when (val res = repository.insertMyPokemon(nickname, pokemonId)) {
            is DataResource.Error -> DataResource.Error(res.uiText)
            is DataResource.Success -> {
                val successMsg = UiText.DynamicString("Success add $nickname to your pokemon list!")
                DataResource.Success(successMsg)
            }
        }
    }
}
