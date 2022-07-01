package com.jowies.springkurs.service

import com.jowies.springkurs.client.PokemonClient
import com.jowies.springkurs.dto.mapper.toPokemonDTOMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service

@Service
class PokemonService(private val pokemonClient: PokemonClient) {
    suspend fun getSinglePokemon(name: String) =
        toPokemonDTOMapper(pokemonClient.getSinglePokemon(name))

    suspend fun getAllPokemonInEggGroup(eggGroupName: String) = coroutineScope {
        val eggGroup = pokemonClient.getEggGroup(eggGroupName)

        eggGroup.pokemonSpecies.map {
            async {
                pokemonClient.getSinglePokemon(it.name);
            }
        }.awaitAll().map { toPokemonDTOMapper(it) }
    }
}
