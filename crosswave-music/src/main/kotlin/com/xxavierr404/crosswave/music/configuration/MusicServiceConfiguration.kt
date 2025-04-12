package com.xxavierr404.crosswave.music.configuration

import com.xxavierr404.crosswave.kafka.events.model.music.MusicEvent
import com.xxavierr404.crosswave.music.dao.MusicDao
import com.xxavierr404.crosswave.music.service.MusicService
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.KafkaTemplate
import org.xxavierr404.crosswave.ai.client.apis.DefaultApi
import java.util.*

@Configuration
@Import(KafkaConfiguration::class)
class MusicServiceConfiguration {
    @Bean
    @LoadBalanced
    fun aiClient() = DefaultApi("lb://crosswave-ai")

    @Bean
    fun musicService(
        musicDao: MusicDao,
        kafkaTemplate: KafkaTemplate<UUID, MusicEvent>,
        aiClient: DefaultApi,
    ) =
        MusicService(musicDao, kafkaTemplate, aiClient)
}