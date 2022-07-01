package com.jowies.springkurs.config

import com.jowies.springkurs.client.PokemonClient
import com.jowies.springkurs.properties.PokemonClientProperties
import org.apache.hc.client5.http.auth.AuthScope
import org.apache.hc.client5.http.auth.NTCredentials
import org.apache.hc.client5.http.auth.StandardAuthScheme
import org.apache.hc.client5.http.impl.async.HttpAsyncClients
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider
import org.apache.hc.core5.http.HttpHost
import org.apache.hc.core5.http2.HttpVersionPolicy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.client.reactive.HttpComponentsClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient


private val logger = mu.KotlinLogging.logger {}

@Configuration
class PokemonClientConfig(
    private val pokemonClientProperties: PokemonClientProperties
) {
    @Bean
    fun pokemonClient(): PokemonClient {
        val size = 16 * 1024 * 1024
        val strategies = ExchangeStrategies.builder()
            .codecs { codecs: ClientCodecConfigurer ->
                codecs.defaultCodecs().maxInMemorySize(size)
            }
            .build()
        val webClient = WebClient
            .builder()
            .exchangeStrategies(strategies)
            .baseUrl(pokemonClientProperties.url)
            .build()
        return PokemonClient(
            webClient
        )
    }
}
