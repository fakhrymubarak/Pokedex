package com.fakhry.pokedex.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fakhry.pokedex.domain.model.MyPokemon
import com.fakhry.pokedex.domain.model.Pokemon

@Entity(tableName = "my_pokemon_entity")
data class MyPokemonEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "my_pokemon_id")
    var myPokemonId: Int = 0,

    @ColumnInfo(name = "nickname")
    var nickname: String,

    @ColumnInfo(name = "pokemon_id")
    var pokemonId: Int,
)

fun MyPokemonEntity.mapToDomain(pokemon: Pokemon) = MyPokemon(myPokemonId, nickname, pokemon)
