package com.xxavierr404.crosswave.auth

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@OpenAPIDefinition(
	servers = [
		Server(url = "/", description = "Default server")
	]
)
@SpringBootApplication
@EnableConfigurationProperties
class CrosswaveApplication

fun main(args: Array<String>) {
	runApplication<CrosswaveApplication>(*args)
}
