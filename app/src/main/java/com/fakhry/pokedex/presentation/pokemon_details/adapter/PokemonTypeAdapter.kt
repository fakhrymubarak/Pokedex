package com.fakhry.pokedex.presentation.pokemon_details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.pokedex.databinding.ItemPokemonTypeBinding
import com.fakhry.pokedex.domain.model.PokemonType

class PokemonTypeAdapter : RecyclerView.Adapter<PokemonTypeAdapter.ViewHolder>() {
    private val listData = ArrayList<PokemonType>()

    fun setData(newListData: List<PokemonType>) {
        val previousContentSize = listData.size
        listData.clear()
        listData.addAll(newListData)
        notifyItemRangeRemoved(0, previousContentSize)
        notifyItemRangeInserted(0, newListData.size)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemPokemonTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size

    inner class ViewHolder(private val binding: ItemPokemonTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: PokemonType) {
            initView(data)
        }

        private fun initView(data: PokemonType) {
            binding.tvPokemonType.text = data.name
        }
    }
}
