package com.fakhry.pokedex.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyPokemon(
    val nickname: String,
    val pokemon: Pokemon,
) : Parcelable
