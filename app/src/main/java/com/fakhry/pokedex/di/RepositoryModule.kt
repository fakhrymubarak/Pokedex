package com.fakhry.pokedex.di

import com.fakhry.pokedex.data.repository.PokemonRepositoryImpl
import com.fakhry.pokedex.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providePokemonRepository(pokemonRepository: PokemonRepositoryImpl): PokemonRepository

}