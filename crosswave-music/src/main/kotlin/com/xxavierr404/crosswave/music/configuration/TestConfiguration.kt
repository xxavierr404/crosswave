package com.xxavierr404.crosswave.music.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.xxavierr404.crosswave.auth.client.apis.DefaultApi

@Configuration
class TestConfiguration {
    @Bean
    fun authClient(): DefaultApi = DefaultApi("auth")
}