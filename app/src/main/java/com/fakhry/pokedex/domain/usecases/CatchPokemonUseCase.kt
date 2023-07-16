package com.fakhry.pokedex.domain.usecases

import com.fakhry.pokedex.state.UiResult
import com.fakhry.pokedex.core.enums.UiText
import com.fakhry.pokedex.domain.repository.PokemonRepository
import com.fakhry.pokedex.state.HttpClientResult
import kotlinx.coroutines.delay
import javax.inject.Inject

class CatchPokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(): UiResult<UiText> {
        delay(1500)
        val isCaught = Math.random() > 0.5

        return if (isCaught) {
            val listOfSuccessMessage = listOf(
                "Gotcha! Pokémon was caught!",
                "All right! Pokémon was caught!",
            )
            UiResult.Success(UiText.DynamicString(listOfSuccessMessage.random()))
        } else {
            val listOfFailedMessage = listOf(
                "Oh no! The Pokémon broke free! Try again!",
                "Aargh! Almost had it! Try Again!",
            )
            UiResult.Error(UiText.DynamicString(listOfFailedMessage.random()))
        }
    }

    suspend fun saveMyPokemon(_nickname: String, pokemonId: Int): UiResult<UiText> {
        val nickname = _nickname.trim()

        if (nickname.isEmpty()) {
            val emptyTextError = UiText.DynamicString("Please fill the nickname of your Pokémon!")
            return UiResult.Error(emptyTextError)
        }

        return when (repository.insertMyPokemon(nickname, pokemonId)) {
            is HttpClientResult.Failure -> UiResult.Error()
            is HttpClientResult.Success -> {
                val successMsg = UiText.DynamicString("Success add $nickname to your pokemon list!")
                UiResult.Success(successMsg)
            }
        }
    }
}
