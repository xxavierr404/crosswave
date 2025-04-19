package com.xxavierr404.crosswave.analytics

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
class CrosswaveAnalyticsApplication

fun main(args: Array<String>) {
	runApplication<CrosswaveAnalyticsApplication>(*args)
}
