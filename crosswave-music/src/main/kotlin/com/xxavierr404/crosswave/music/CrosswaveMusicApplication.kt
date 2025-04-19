package com.xxavierr404.crosswave.music

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@OpenAPIDefinition(
	servers = [
		Server(url = "/", description = "Default server")
	]
)
@SpringBootApplication
class CrosswaveMusicApplication

fun main(args: Array<String>) {
	runApplication<CrosswaveMusicApplication>(*args)
}
