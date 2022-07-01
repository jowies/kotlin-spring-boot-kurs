package com.jowies.springkurs.dto

data class PokemonDTO(
    val id: String,
    val name: String,
    val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val numberOfAbilities: Int
)
