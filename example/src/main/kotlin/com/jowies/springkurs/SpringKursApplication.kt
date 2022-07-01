package com.jowies.springkurs

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class SpringKursApplication

fun main(args: Array<String>) {
    runApplication<SpringKursApplication>(*args)
}
