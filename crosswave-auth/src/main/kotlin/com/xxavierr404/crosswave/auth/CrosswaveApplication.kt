package com.xxavierr404.crosswave.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class CrosswaveApplication

fun main(args: Array<String>) {
	runApplication<CrosswaveApplication>(*args)
}
