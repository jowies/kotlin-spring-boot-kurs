package com.jowies.springkurs.client.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class PokemonSpecies(
    val name: String,
    val url: String
)


data class EggGroupResponse(
    val id: Int,
    val name: String,
    @JsonProperty("pokemon_species")
    val pokemonSpecies: List<PokemonSpecies>
)