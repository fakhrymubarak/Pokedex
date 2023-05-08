package com.fakhry.pokedex.presentation.my_pokemon

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fakhry.pokedex.core.enums.EXTRA_POKEMON_ID
import com.fakhry.pokedex.core.enums.UiState
import com.fakhry.pokedex.core.enums.asString
import com.fakhry.pokedex.core.utils.components.showToast
import com.fakhry.pokedex.core.utils.components.viewBinding
import com.fakhry.pokedex.core.utils.isShimmerStarted
import com.fakhry.pokedex.core.utils.isVisible
import com.fakhry.pokedex.databinding.ActivityMyPokemonBinding
import com.fakhry.pokedex.domain.model.MyPokemon
import com.fakhry.pokedex.presentation.my_pokemon.adapter.ItemMyPokemonAdapter
import com.fakhry.pokedex.presentation.pokemon_details.PokemonDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPokemonActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMyPokemonBinding::inflate)
    private val viewModel by viewModels<MyPokemonViewModel>()

    private lateinit var adapter: ItemMyPokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        adapter = ItemMyPokemonAdapter()
        binding.rvPokemon.adapter = adapter
    }

    private fun initListener() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        adapter.onDetailsClick = { data ->
            val intent = Intent(this, PokemonDetailsActivity::class.java)
            intent.putExtra(EXTRA_POKEMON_ID, data.pokemon.id)
            startActivity(intent)
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.getListPokemon()
            viewModel.listMyPokemon.collect { state ->
                when (state) {
                    is UiState.Error -> showToast(state.uiText.asString(this@MyPokemonActivity))
                    is UiState.Loading -> populateLoadingPokemon(state.isLoading)
                    is UiState.Success -> populateSuccess(state.data)
                }
            }
        }
    }

    private fun populateSuccess(data: List<MyPokemon>) {
        adapter.setData(data)
    }

    private fun populateLoadingPokemon(isLoading: Boolean) {
        binding.shimmerList.isShimmerStarted(isLoading)
        binding.rvPokemon.isVisible(!isLoading, true)
    }
}