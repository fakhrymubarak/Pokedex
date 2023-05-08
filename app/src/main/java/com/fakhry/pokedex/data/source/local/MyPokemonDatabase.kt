package com.fakhry.pokedex.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fakhry.pokedex.data.source.local.entity.MyPokemonEntity
import com.fakhry.pokedex.data.source.local.entity.PokemonEntity
import com.fakhry.pokedex.data.source.local.room.PokemonDao


@Database(
    entities = [
        PokemonEntity::class,
        MyPokemonEntity::class
    ], version = 1, exportSchema = false
)
abstract class MyPokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
}