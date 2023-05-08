package com.fakhry.pokedex.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.fakhry.pokedex.core.enums.EXTRA_POKEMON
import com.fakhry.pokedex.core.enums.asString
import com.fakhry.pokedex.core.network.getMessageFromException
import com.fakhry.pokedex.core.utils.components.collectLifecycleFlow
import com.fakhry.pokedex.core.utils.components.showToast
import com.fakhry.pokedex.core.utils.components.viewBinding
import com.fakhry.pokedex.core.utils.isShimmerStarted
import com.fakhry.pokedex.core.utils.isVisible
import com.fakhry.pokedex.databinding.ActivityDashboardBinding
import com.fakhry.pokedex.domain.model.Pokemon
import com.fakhry.pokedex.presentation.dashboard.adapter.ItemLoadStateAdapter
import com.fakhry.pokedex.presentation.dashboard.adapter.PokemonPagingAdapter
import com.fakhry.pokedex.presentation.pokemon_details.PokemonDetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityDashboardBinding::inflate)
    private val viewModel by viewModels<DashboardViewModel>()

    private lateinit var pokemonAdapter: PokemonPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        pokemonAdapter = PokemonPagingAdapter()
        binding.rvPokemon.adapter = pokemonAdapter.withLoadStateFooter(ItemLoadStateAdapter())
    }

    private fun initListener() {
        pokemonAdapter.onDetailClick = {
            val intent = Intent(this, PokemonDetailsActivity::class.java)
            intent.putExtra(EXTRA_POKEMON, it)
            startActivity(intent)
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.listPokemon.collectLatest { data ->
                    if (data == null) {
                        viewModel.getListPokemon()
                        return@collectLatest
                    }
                    populateSuccessPokemon(data)
                }
            }
        }

        collectLifecycleFlow(pokemonAdapter.loadStateFlow) { loadStates ->
            val state = loadStates.refresh
            populateLoadingPokemon(state is LoadState.Loading)
            if (state is LoadState.Error) {
                val networkException = getMessageFromException(state.error as Exception)
                showToast(networkException.errorMessages.asString(this))
                pokemonAdapter.retry()
            }
        }
    }

    private fun populateLoadingPokemon(isLoading: Boolean) {
        binding.shimmerList.isShimmerStarted(isLoading)
        binding.rvPokemon.isVisible(!isLoading)
    }

    private suspend fun populateSuccessPokemon(data: PagingData<Pokemon>) {
        pokemonAdapter.submitData(PagingData.empty())
        pokemonAdapter.submitData(data)
    }
}