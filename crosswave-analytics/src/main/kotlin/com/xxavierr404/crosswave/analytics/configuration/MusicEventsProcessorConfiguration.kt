package com.xxavierr404.crosswave.analytics.configuration

import com.xxavierr404.crosswave.analytics.dao.AnalyticRecordsDao
import com.xxavierr404.crosswave.analytics.kafka.processors.MusicEventsProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(MongoDaoConfiguration::class)
class MusicEventsProcessorConfiguration {
    @Bean
    fun musicEventsProcessor(analyticRecordsDao: AnalyticRecordsDao) = MusicEventsProcessor(analyticRecordsDao)
}