package com.example.myapplication.data

import retrofit2.http.GET

interface PokemonApi {
    @GET("pokedex.json")
    suspend fun getPokedex(): PokedexResponse
}

data class PokedexResponse(
    val pokemon: List<PokemonEntry>
)

data class PokemonEntry(
    val name: String,
    val img: String
)

