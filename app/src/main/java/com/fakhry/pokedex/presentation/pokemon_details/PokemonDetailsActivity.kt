package com.fakhry.pokedex.presentation.pokemon_details

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.fakhry.pokedex.R
import com.fakhry.pokedex.core.enums.EXTRA_POKEMON
import com.fakhry.pokedex.core.utils.components.collectLifecycleFlow
import com.fakhry.pokedex.core.utils.components.viewBinding
import com.fakhry.pokedex.databinding.ActivityPokemonDetailsBinding
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.presentation.pokemon_details.adapter.PhotoAdapter
import com.fakhry.pokedex.presentation.pokemon_details.adapter.PokemonTypeAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PokemonDetailsActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityPokemonDetailsBinding::inflate)
    private val viewModel by viewModels<PokemonDetailsViewModel>()

    private lateinit var pokemon: Pokemon
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
        pokemon = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S_V2) {
            intent?.getParcelableExtra(EXTRA_POKEMON, Pokemon::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent?.getParcelableExtra(EXTRA_POKEMON)
        } ?: return onBackPressedDispatcher.onBackPressed()

        binding.apply {
            tvPokemonName.text = pokemon.name

            typeAdapter = PokemonTypeAdapter()
            rvTypes.adapter = typeAdapter
            typeAdapter.setData(pokemon.types)

            photoAdapter = PhotoAdapter()
            vpPhotoSlider.adapter = photoAdapter
            binding.dotsPhoto.attachTo(binding.vpPhotoSlider)
            photoAdapter.setData(pokemon.pictures)
            viewModel.setTotalPictures(pokemon.pictures.size)

            tvPokemonWeightValue.text = getString(R.string.text_weight, pokemon.weight)
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
        }
    }

    private fun initObserver() {
        viewModel.runCarousel()
        collectLifecycleFlow(viewModel.carouselState) {
            binding.vpPhotoSlider.currentItem = it
        }
    }
}