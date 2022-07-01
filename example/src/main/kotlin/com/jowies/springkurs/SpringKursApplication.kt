package com.jowies.springkurs

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties

@SpringBootApplication
@EnableConfigurationProperties
class SpringKursApplication

fun main(args: Array<String>) {
    val app = SpringApplication(SpringKursApplication::class.java)
    val environment = System.getenv("ENVIRONMENT").lowercase()

    app.setAdditionalProfiles(environment)
    app.run(*args)
}
