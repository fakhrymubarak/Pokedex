package com.fakhry.pokedex.presentation.my_pokemon.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.pokedex.R
import com.fakhry.pokedex.core.utils.loadWithShimmer
import com.fakhry.pokedex.databinding.ItemMyPokemonBinding
import com.fakhry.pokedex.domain.model.MyPokemon

class ItemMyPokemonAdapter : RecyclerView.Adapter<ItemMyPokemonAdapter.ViewHolder>() {
    private val listData = ArrayList<MyPokemon>()

    var onDetailsClick: ((MyPokemon) -> Unit)? = null
    var onRelease: ((MyPokemon) -> Unit)? = null

    fun setData(newListData: List<MyPokemon>) {
        val previousContentSize = this.listData.size
        this.listData.clear()
        this.listData.addAll(newListData)
        notifyItemRangeRemoved(0, previousContentSize)
        notifyItemRangeInserted(0, newListData.size)
    }

    fun removeData(pokemon: MyPokemon) {
        val index = listData.indexOf(pokemon)
        this.listData.remove(pokemon)
        notifyItemRangeRemoved(index, 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemMyPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size

    inner class ViewHolder(private val binding: ItemMyPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MyPokemon) {
            initView(data)
            initListener(data)
        }

        private fun initView(data: MyPokemon) {
            binding.apply {
                ivPokemonFront.loadWithShimmer(data.pokemon.frontImage)
                tvPokemonId.text = tvPokemonId.context.getString(R.string.text_no_pokemon, data.pokemon.id)
                tvNickname.text = data.nickname
            }
        }

        private fun initListener(data: MyPokemon) {
            binding.root.setOnClickListener {
                onDetailsClick?.invoke(data)
            }

            binding.btnRelease.setOnClickListener {
                onRelease?.invoke(data)
            }
        }
    }
}