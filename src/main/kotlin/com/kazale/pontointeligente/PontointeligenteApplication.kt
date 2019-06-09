package com.kazale.pontointeligente

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@SpringBootApplication
@EnableWebSecurity
class PontointeligenteApplication

fun main(args: Array<String>) {
	runApplication<PontointeligenteApplication>(*args)
}

