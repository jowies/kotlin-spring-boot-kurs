package com.jowies.springkurs.client.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class Ability(
    val name: String
)

data class AbilityInfo(
    val ability: Ability
)

data class PokemonResponse(
    val id: String,
    val name: String,
    @JsonProperty("base_experience")
    val baseExperience: Int,
    val height: Int,
    val is_default: Boolean,
    val weight: Int,
    val abilities: List<AbilityInfo>
)
