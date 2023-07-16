package com.fakhry.pokedex.presentation.pokemon_details

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import com.fakhry.pokedex.R
import com.fakhry.pokedex.state.UiResult
import com.fakhry.pokedex.core.enums.asString
import com.fakhry.pokedex.core.utils.components.collectLifecycleFlow
import com.fakhry.pokedex.core.utils.components.showToast
import com.fakhry.pokedex.core.utils.isVisible
import com.fakhry.pokedex.databinding.DialogNicknameBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NicknameDialog : BottomSheetDialogFragment() {
    private lateinit var binding: DialogNicknameBinding
    private val viewModel by activityViewModels<PokemonDetailsViewModel>()


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        binding = DialogNicknameBinding.inflate(LayoutInflater.from(requireContext()), null, false)
        dialog.setContentView(binding.root)
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        isCancelable = false
        return dialog
    }

    override fun onStart() {
        super.onStart()

        initListener()
        initObserver()
    }


    private fun initListener() {
        binding.btnSave.setOnClickListener {
            viewModel.saveMyPokemon(binding.etNickname.text.toString())
        }
    }

    private fun initObserver() {
        collectLifecycleFlow(viewModel.savePokemonState) { state ->
            when (state) {
                is UiResult.Error -> {
                    binding.tvSaveErrMessage.isVisible(true)
                    binding.tvSaveErrMessage.text = state.uiText.asString(requireContext())
                }
                is UiResult.Loading -> {}
                is UiResult.Success -> {
                    binding.tvSaveErrMessage.isVisible(false, isGone = true)
                    showToast(state.data.asString(requireContext()))
                    dialog?.dismiss()
                }
            }
        }
    }
}