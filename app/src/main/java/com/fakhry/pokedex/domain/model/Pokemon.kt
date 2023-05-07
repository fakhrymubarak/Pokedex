package com.fakhry.pokedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pokemon(
    val id: Int,
    val name: String,
    val weight: Int = 0,
    val frontImage: String = "",
    val pictures: List<String> = listOf(),
    val types: List<PokemonType> = listOf(),
) : Parcelable
