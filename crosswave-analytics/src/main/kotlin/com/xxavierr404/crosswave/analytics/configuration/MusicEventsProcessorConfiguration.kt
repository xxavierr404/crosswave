package com.xxavierr404.crosswave.analytics.configuration

import com.xxavierr404.crosswave.analytics.kafka.processors.MusicEventsProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MusicEventsProcessorConfiguration {
    @Bean
    fun musicEventsProcessor() = MusicEventsProcessor()
}