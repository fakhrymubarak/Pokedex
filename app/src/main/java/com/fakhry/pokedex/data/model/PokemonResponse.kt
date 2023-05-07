package com.fakhry.pokedex.data.model

import com.fakhry.pokedex.core.utils.capitalized
import com.fakhry.pokedex.domain.model.Pokemon
import com.google.gson.annotations.SerializedName
import java.util.*

data class PokemonResponse(

    @field:SerializedName("next")
    val next: String? = null,

    @field:SerializedName("previous")
    val previous: String? = null,

    @field:SerializedName("count")
    val count: Int,

    @field:SerializedName("results")
    val results: List<PokemonData>
)

data class PokemonData(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("url")
    val url: String
)

fun PokemonData.mapToDomain(): Pokemon {
    val listPath = url.split('/').toMutableList()
    listPath.removeLast()
    val id = listPath.last().toInt()
    return Pokemon(
        id = id,
        name = name.capitalized(),
    )
}
