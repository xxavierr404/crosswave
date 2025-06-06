package com.xxavierr404.crosswave.auth.configuration

import com.xxavierr404.crosswave.kafka.events.model.auth.AuthEvent
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.UUIDSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer
import java.util.*

@Configuration
class KafkaConfiguration {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var kafkaAddress: String

    @Bean
    fun authEventsTopic(): NewTopic = NewTopic("auth-events", 1, 3)

    @Bean
    fun producerFactory(): ProducerFactory<UUID, AuthEvent> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    @Bean
    fun producerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaAddress
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
    fun kafkaTemplate(): KafkaTemplate<UUID, AuthEvent> {
        return KafkaTemplate(producerFactory())
    }
}