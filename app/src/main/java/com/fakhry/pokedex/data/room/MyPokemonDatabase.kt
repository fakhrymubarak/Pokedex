package com.fakhry.pokedex.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fakhry.pokedex.data.room.entity.MyPokemonEntity
import com.fakhry.pokedex.data.room.entity.PokemonEntity


@Database(
    entities = [
        PokemonEntity::class,
        MyPokemonEntity::class
    ], version = 1, exportSchema = false
)
abstract class MyPokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
}