package com.fakhry.pokedex.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val weight: Double = 0.0,
    val frontImage: String = "",
    val pictures: List<String> = listOf(),
    val types: List<PokemonType> = listOf(),
)