package com.fakhry.pokedex.presentation.pokemon_details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.fakhry.pokedex.core.enums.EXTRA_POKEMON_ID
import com.fakhry.pokedex.core.enums.asString
import com.fakhry.pokedex.core.utils.components.collectLifecycleFlow
import com.fakhry.pokedex.core.utils.components.showToast
import com.fakhry.pokedex.core.utils.components.viewBinding
import com.fakhry.pokedex.core.utils.isShimmerStarted
import com.fakhry.pokedex.databinding.ActivityPokemonDetailsBinding
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.state.UiResult
import com.fakhry.pokedex.theme.PokedexAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PokemonDetailsActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPokemonDetailsBinding::inflate)
    private val viewModel by viewModels<PokemonDetailsViewModel>()

    private var pokemonId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        pokemonId = intent.getIntExtra(EXTRA_POKEMON_ID, -1)
        if (pokemonId == -1) return onBackPressedDispatcher.onBackPressed()
    }

    private fun initListener() {
        binding.composeButton.setContent {
            PokedexAppTheme {
                ButtonCatch(onClick = { viewModel.catchPokemon() })
            }
        }
    }

    private fun initObserver() {
        viewModel.runCarousel()

        // todo update carousel index jetpack compose
        collectLifecycleFlow(viewModel.carouselState) { index -> }

        viewModel.getPokemonDetails(pokemonId)
        collectLifecycleFlow(viewModel.pokemonDetailsState) { state ->
            when (state) {
                is UiResult.Error -> showToast(state.uiText.asString(this))
                is UiResult.Loading -> binding.shimmerDetails.isShimmerStarted(state.isLoading)
                is UiResult.Success -> populatePokemonDetails(state.data)
            }
        }
        collectLifecycleFlow(viewModel.catchPokemonState) { state ->
            when (state) {
                is UiResult.Error -> updateCatchMessage(state.uiText.asString(this), false)
                is UiResult.Loading -> populateLoadingButton(state.isLoading)
                is UiResult.Success -> {
                    updateCatchMessage(state.data.asString(this), true)
                    showBottomSheetNickname()
                }
            }
        }
    }

    private fun updateCatchMessage(message: String, isSuccess: Boolean) {
        binding.composeTextError.setContent {
            PokedexAppTheme {
                ErrorMessage(message, isSuccess)
            }
        }
    }

    private fun populateLoadingButton(isLoading: Boolean) {
        binding.composeButton.setContent {
            PokedexAppTheme {
                ButtonCatch(onClick = { viewModel.catchPokemon() }, isLoading)
            }
        }
    }

    private fun populatePokemonDetails(pokemon: Pokemon) {
        binding.apply {
            viewModel.setTotalPictures(pokemon.pictures.size)

            composeHeader.setContent {
                PokedexAppTheme {
                    HeaderSection(
                        images = pokemon.pictures,
                        onBackClick = {
                            onBackPressedDispatcher.onBackPressed()
                        }
                    )
                }
            }
            composeBody.setContent {
                PokedexAppTheme {
                    BodySection(pokemon = pokemon)
                }
            }
        }
    }

    private fun showBottomSheetNickname() {
        val sheet = NicknameDialog()
        if (!sheet.isAdded) sheet.show(supportFragmentManager, sheet.tag)
    }
}