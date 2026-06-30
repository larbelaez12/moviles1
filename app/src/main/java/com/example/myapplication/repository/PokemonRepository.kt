package com.example.myapplication.repository

import com.example.myapplication.data.PokemonApi
import com.example.myapplication.data.PokemonEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PokemonRepository(
    private val api: PokemonApi
) {
    private var cached: List<PokemonEntry> = emptyList()

    suspend fun getRandomPokemon(): PokemonEntry? = withContext(Dispatchers.IO) {
        if (cached.isEmpty()) {
            cached = api.getPokedex().pokemon.map { entry ->
                entry.copy(img = entry.img.replace("http://", "https://"))
            }
        }
        if (cached.isEmpty()) return@withContext null
        cached.random()
    }
}

