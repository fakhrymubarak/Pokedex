package com.fakhry.pokedex.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fakhry.pokedex.core.utils.capitalized
import com.fakhry.pokedex.domain.model.Pokemon

@Entity(tableName = "pokemon_entity")
data class PokemonEntity(
    @PrimaryKey
    @ColumnInfo(name = "pokemon_id")
    var pokemonId: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "weight")
    var weight: Int,

    @ColumnInfo(name = "front_image")
    var frontImage: String,
)

fun PokemonEntity.mapToDomain(): Pokemon {
    return Pokemon(
        id = pokemonId,
        name = name.capitalized(),
        weight = weight/10.0,
        frontImage = frontImage,
    )
}