package com.jowies.springkurs.dto.mapper

import com.jowies.springkurs.client.domain.PokemonResponse
import com.jowies.springkurs.dto.PokemonDTO

fun toPokemonDTOMapper(pokemonResponse: PokemonResponse): PokemonDTO {
    return PokemonDTO(
        id = pokemonResponse.id,
        name = pokemonResponse.name,
        baseExperience = pokemonResponse.baseExperience,
        height = pokemonResponse.height,
        weight = pokemonResponse.weight,
        numberOfAbilities = pokemonResponse.abilities.count()
    )
}
