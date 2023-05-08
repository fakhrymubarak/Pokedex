package com.fakhry.pokedex.di

import android.content.Context
import androidx.room.Room
import com.fakhry.pokedex.data.source.local.MyPokemonDatabase
import com.fakhry.pokedex.data.source.local.room.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): MyPokemonDatabase {
        val databaseName = "MyPokemonDatabase.db"

        return Room.databaseBuilder(context, MyPokemonDatabase::class.java, databaseName)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun providePokemonDao(database: MyPokemonDatabase): PokemonDao =
        database.pokemonDao()
}

