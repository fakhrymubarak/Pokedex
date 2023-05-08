package com.fakhry.pokedex.core.utils.components

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 * Flow collector for BottomSheetDialogFragment
 * @see [launch]
 *
 * @param flow flow to be collected
 * @param collect lambda function to collect items
 * */
fun <T> BottomSheetDialogFragment.collectLifecycleFlow(flow: Flow<T>, collect: FlowCollector<T>) {
    this.lifecycleScope.launch {
        flow.collect(collect)
    }
}

fun Fragment.showToast(message: String) {
    this.requireContext().showToast(message)
}