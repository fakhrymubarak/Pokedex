package com.fakhry.pokedex.presentation.pokemon_details.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fakhry.pokedex.core.utils.loadWithShimmer
import com.fakhry.pokedex.databinding.ItemPhotoBinding

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {
    private val listData = ArrayList<String>()

    fun setData(newListData: List<String>) {
        val previousContentSize = listData.size
        listData.clear()
        listData.addAll(newListData)
        notifyItemRangeRemoved(0, previousContentSize)
        notifyItemRangeInserted(0, newListData.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount() = listData.size

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: String) {
            binding.ivPhoto.loadWithShimmer(data)
        }
    }
}