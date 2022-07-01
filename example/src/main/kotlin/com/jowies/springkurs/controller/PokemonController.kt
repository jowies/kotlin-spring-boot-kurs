package com.jowies.springkurs.controller

import com.jowies.springkurs.service.PokemonService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1")
class PokemonController(private val pokemonService: PokemonService) {
    @GetMapping("pokemon/{name}")
    suspend fun getSinglePokemon(@PathVariable name: String) =
        pokemonService.getSinglePokemon(name)

    @GetMapping("egg-group/{name}")
    suspend fun getAllPokemonInEggGroup(@PathVariable name: String) =
        pokemonService.getAllPokemonInEggGroup(name)
}
