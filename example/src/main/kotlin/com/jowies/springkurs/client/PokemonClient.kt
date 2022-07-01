package com.jowies.springkurs.client

import com.jowies.springkurs.client.domain.EggGroupResponse
import com.jowies.springkurs.client.domain.PokemonResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

class PokemonClient(
    private val webClient: WebClient
) {
    suspend fun getSinglePokemon(name: String) =
        webClient
            .get()
            .uri("/pokemon/$name")
            .retrieve()
            .awaitBody<PokemonResponse>()

    suspend fun getEggGroup(name: String) =
        webClient
            .get()
            .uri("/egg-group/$name")
            .retrieve()
            .awaitBody<EggGroupResponse>()
}
