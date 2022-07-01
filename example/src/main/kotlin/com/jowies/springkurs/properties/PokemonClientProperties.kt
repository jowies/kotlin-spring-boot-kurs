package com.jowies.springkurs.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "pokemon.client")
@Component
class PokemonClientProperties {
    lateinit var url: String
}
