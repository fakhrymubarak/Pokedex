package com.fakhry.pokedex.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val weight: Int,
    val frontImage: String,
    val types: List<PokemonType>
)
