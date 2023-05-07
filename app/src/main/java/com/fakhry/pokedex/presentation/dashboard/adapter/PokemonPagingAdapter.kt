package com.fakhry.pokedex.presentation.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.pokedex.databinding.ItemPokemonBinding
import com.fakhry.pokedex.domain.model.Pokemon

class PokemonPagingAdapter : PagingDataAdapter<Pokemon, PokemonPagingAdapter.ViewHolder>(
    PokemonDiffCallback()
) {

    var onDetailClick: ((Int) -> Unit)? = null
    var onLoadDetails: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    inner class ViewHolder(
        private val binding: ItemPokemonBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Pokemon) {
            initView(data)
            initListener(data)
        }

        private fun initView(data: Pokemon) {
            binding.apply {
                onLoadDetails?.invoke(data.id)
                tvPokemonName.text = data.name
            }
        }

        private fun initListener(data: Pokemon) {
            binding.apply {
                root.setOnClickListener {
                    onDetailClick?.invoke(data.id)
                }
            }
        }
    }
}

class PokemonDiffCallback : DiffUtil.ItemCallback<Pokemon>() {
    override fun areItemsTheSame(
        oldItem: Pokemon,
        newItem: Pokemon,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Pokemon,
        newItem: Pokemon,
    ): Boolean = oldItem == newItem
}
