package com.fakhry.pokedex.presentation.my_pokemon

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fakhry.pokedex.R
import com.fakhry.pokedex.core.enums.EXTRA_POKEMON_ID
import com.fakhry.pokedex.state.UiResult
import com.fakhry.pokedex.core.enums.asString
import com.fakhry.pokedex.core.utils.components.collectLifecycleFlow
import com.fakhry.pokedex.core.utils.components.showToast
import com.fakhry.pokedex.core.utils.components.viewBinding
import com.fakhry.pokedex.core.utils.isShimmerStarted
import com.fakhry.pokedex.core.utils.isVisible
import com.fakhry.pokedex.databinding.ActivityMyPokemonBinding
import com.fakhry.pokedex.domain.model.MyPokemon
import com.fakhry.pokedex.presentation.my_pokemon.adapter.ItemMyPokemonAdapter
import com.fakhry.pokedex.presentation.pokemon_details.PokemonDetailsActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

        adapter.onRelease = { data ->
            showDialogRelease(data)
        }
    }

    private fun showDialogRelease(data: MyPokemon) {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.text_release_pokemon))
            .setCancelable(false)
            .setMessage(getString(R.string.text_release_pokemon_desc, data.nickname))
            .setPositiveButton(getString(R.string.text_release)) { _, _ ->
                viewModel.releasePokemon(data)
            }
            .setNegativeButton(getString(R.string.text_no_thanks)) { dialog, _ ->
                dialog.cancel()
            }.show()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.getListPokemon()
            viewModel.listMyPokemon.collect { state ->
                when (state) {
                    is UiResult.Error -> showToast(state.uiText.asString(this@MyPokemonActivity))
                    is UiResult.Loading -> populateLoadingPokemon(state.isLoading)
                    is UiResult.Success -> populateSuccess(state.data)
                }
            }
        }

        collectLifecycleFlow(viewModel.releasePokemonState) { state ->
            when (state) {
                is UiResult.Error -> showToast(state.uiText.asString(this))
                is UiResult.Loading -> {}
                is UiResult.Success -> adapter.removeData(state.data)
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