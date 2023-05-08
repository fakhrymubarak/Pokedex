package com.fakhry.pokedex.data.source.local.room

import androidx.room.*
import com.fakhry.pokedex.data.source.local.entity.MyPokemonEntity
import com.fakhry.pokedex.data.source.local.entity.PokemonEntity

@Dao
interface PokemonDao {

    // Pokemon
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertPokemon(pokemon: PokemonEntity)

    @Query("SELECT * FROM pokemon_entity WHERE pokemon_id = :id")
    fun getPokemon(id: Int): PokemonEntity?

    // My Pokemon
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertMyPokemon(pokemon: MyPokemonEntity)

    @Query("SELECT * FROM my_pokemon_entity ORDER BY my_pokemon_id ASC")
    fun getMyPokemon(): List<MyPokemonEntity>

    @Delete
    fun deleteMyPokemon(pokemon: MyPokemonEntity)
}