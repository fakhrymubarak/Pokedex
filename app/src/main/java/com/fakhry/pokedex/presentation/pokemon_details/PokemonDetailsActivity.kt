package com.fakhry.pokedex.presentation.pokemon_details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.fakhry.pokedex.R
import com.fakhry.pokedex.core.enums.EXTRA_POKEMON_ID
import com.fakhry.pokedex.core.enums.UiState
import com.fakhry.pokedex.core.enums.asString
import com.fakhry.pokedex.core.utils.components.collectLifecycleFlow
import com.fakhry.pokedex.core.utils.components.showToast
import com.fakhry.pokedex.core.utils.components.viewBinding
import com.fakhry.pokedex.core.utils.isShimmerStarted
import com.fakhry.pokedex.core.utils.isVisible
import com.fakhry.pokedex.databinding.ActivityPokemonDetailsBinding
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.presentation.pokemon_details.adapter.PhotoAdapter
import com.fakhry.pokedex.presentation.pokemon_details.adapter.PokemonTypeAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PokemonDetailsActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPokemonDetailsBinding::inflate)
    private val viewModel by viewModels<PokemonDetailsViewModel>()

    private var pokemonId: Int = -1
    private lateinit var typeAdapter: PokemonTypeAdapter
    private lateinit var photoAdapter: PhotoAdapter


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
        binding.apply {
            typeAdapter = PokemonTypeAdapter()
            rvTypes.adapter = typeAdapter

            photoAdapter = PhotoAdapter()
            vpPhotoSlider.adapter = photoAdapter
            binding.dotsPhoto.attachTo(binding.vpPhotoSlider)
        }
    }

    private fun initListener() {
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.vpPhotoSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCarouselCurrentIndex(position)
                super.onPageSelected(position)
            }
        })


        binding.btnCatchPokemon.setOnClickListener {
            viewModel.catchPokemon()
        }
    }

    private fun initObserver() {
        viewModel.runCarousel()
        collectLifecycleFlow(viewModel.carouselState) { i -> binding.vpPhotoSlider.currentItem = i }

        viewModel.getPokemonDetails(pokemonId)
        collectLifecycleFlow(viewModel.pokemonDetailsState) { state ->
            when (state) {
                is UiState.Error -> showToast(state.uiText.asString(this))
                is UiState.Loading -> binding.shimmerDetails.isShimmerStarted(state.isLoading)
                is UiState.Success -> populatePokemonDetails(state.data)
            }
        }
        collectLifecycleFlow(viewModel.catchPokemonState) { state ->
            when (state) {
                is UiState.Error -> updateCatchMessage(state.uiText.asString(this), false)
                is UiState.Loading -> populateLoadingButton(state.isLoading)
                is UiState.Success -> {
                    updateCatchMessage(state.data.asString(this), true)
                    showBottomSheetNickname()
                }
            }
        }
    }

    private fun updateCatchMessage(message: String, isSuccess: Boolean) {
        binding.tvCatchMessage.apply {
            text = message

            val textColor = if (isSuccess) R.color.color_green50 else R.color.color_red50
            setTextColor(ContextCompat.getColor(this@PokemonDetailsActivity, textColor))
        }
    }

    private fun populateLoadingButton(isLoading: Boolean) {
        binding.tvCatchMessage.isVisible(true)
        binding.btnCatchPokemon.isEnabled = !isLoading
        binding.pbLoading.isVisible(isLoading, true)
    }

    private fun populatePokemonDetails(pokemon: Pokemon) {
        binding.apply {
            tvPokemonName.text = pokemon.name
            typeAdapter.setData(pokemon.types)
            photoAdapter.setData(pokemon.pictures)
            viewModel.setTotalPictures(pokemon.pictures.size)
            tvPokemonWeightValue.text = getString(R.string.text_weight_value, pokemon.weight)
        }
    }

    private fun showBottomSheetNickname() {
        val sheet = NicknameDialog()
        if (!sheet.isAdded) sheet.show(supportFragmentManager, sheet.tag)
    }
}