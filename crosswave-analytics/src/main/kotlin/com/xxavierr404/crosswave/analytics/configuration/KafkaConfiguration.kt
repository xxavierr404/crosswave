package com.xxavierr404.crosswave.analytics.configuration

import com.xxavierr404.crosswave.analytics.dao.UserProfileDao
import com.xxavierr404.crosswave.analytics.kafka.processors.AuthEventsProcessor
import com.xxavierr404.crosswave.kafka.events.model.auth.AuthEvent
import com.xxavierr404.crosswave.kafka.events.model.music.MusicEvent
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDDeserializer
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.*


@Configuration
@EnableKafka
class KafkaConfiguration {
    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapAddress: String

    @Bean
    fun authEventConsumerFactory(): ConsumerFactory<UUID, AuthEvent> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.GROUP_ID_CONFIG] = "analytics-group"
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress

        val deserializer = JsonDeserializer<AuthEvent>()
        deserializer.addTrustedPackages(
            "com.xxavierr404.crosswave.kafka.events.model.auth"
        )

        return DefaultKafkaConsumerFactory(props, UUIDDeserializer(), deserializer)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<UUID, AuthEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<UUID, AuthEvent>()
        factory.consumerFactory = authEventConsumerFactory()
        return factory
    }

    @Bean
    fun registrationEventProcessor(
        profileDao: UserProfileDao,
    ) = AuthEventsProcessor(profileDao)

    @Bean
    fun musicEventsTopic(): NewTopic = NewTopic("music-events", 1, 3)

    @Bean
    fun musicEventConsumerFactory(): ConsumerFactory<UUID, MusicEvent> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.GROUP_ID_CONFIG] = "analytics-group"
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress

        val deserializer = JsonDeserializer<MusicEvent>()
        deserializer.addTrustedPackages(
            "com.xxavierr404.crosswave.kafka.events.model.music"
        )

        return DefaultKafkaConsumerFactory(props, UUIDDeserializer(), deserializer)
    }

    @Bean
    fun musicKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<UUID, MusicEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<UUID, MusicEvent>()
        factory.consumerFactory = musicEventConsumerFactory()
        return factory
    }

    @Bean
    fun producerFactory(): ProducerFactory<UUID, MusicEvent> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    @Bean
    fun producerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = UUIDSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return props
    }

    @Bean
    fun kafkaAdmin(
        kafkaProperties: KafkaProperties,
    ): KafkaAdmin {
        return KafkaAdmin(kafkaProperties.properties as Map<String, Any>)
    }

    @Bean
    fun musicEventKafkaTemplate(): KafkaTemplate<UUID, MusicEvent> {
        return KafkaTemplate(producerFactory())
    }
}