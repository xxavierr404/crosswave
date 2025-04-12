package com.xxavierr404.crosswave.analytics

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
class CrosswaveAnalyticsApplication

fun main(args: Array<String>) {
	runApplication<CrosswaveAnalyticsApplication>(*args)
}
