package com.fakhry.pokedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonType(
    val id: Int,
    val name: String,
) : Parcelable