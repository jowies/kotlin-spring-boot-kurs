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
    @Profile("!local")
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

    @Bean
    @Profile("local")
    fun pokemonClientLocal(): PokemonClient {
        logger.info { "Local" }
        val client =
            HttpAsyncClients
                .custom().setProxy(host)
                .setDefaultCredentialsProvider(
                    BasicCredentialsProvider().apply {
                        setCredentials(scope, credentials)
                    }
                )
                .setVersionPolicy(HttpVersionPolicy.FORCE_HTTP_1)
                .build()
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
            .clientConnector(HttpComponentsClientHttpConnector(client))
            .build()
        return PokemonClient(
            webClient
        )
    }

    companion object {
        const val WEBGUARD_PROXY_HOST = "webguard.gjensidige.no"
        const val WEBGUARD_PROXY_PORT = 8080

        val host = HttpHost(WEBGUARD_PROXY_HOST, WEBGUARD_PROXY_PORT)
        val scope = AuthScope(null, "webguard.gjensidige.no", 8080, "", StandardAuthScheme.NTLM)
        val credentials = NTCredentials("aa", "bb".toCharArray(), "localhost", "gjensidige.no")
    }
}
