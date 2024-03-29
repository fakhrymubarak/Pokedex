package com.fakhry.pokedex.domain.model

import com.fakhry.pokedex.data.room.entity.MyPokemonEntity

data class MyPokemon(
    val id: Int,
    val nickname: String,
    val pokemon: Pokemon,
)

fun MyPokemon.mapToEntity() = MyPokemonEntity(id, nickname, pokemon.id)