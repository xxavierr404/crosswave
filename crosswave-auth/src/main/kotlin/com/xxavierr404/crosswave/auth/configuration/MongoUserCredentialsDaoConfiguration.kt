package com.xxavierr404.crosswave.auth.configuration

import com.mongodb.MongoClientSettings
import com.mongodb.kotlin.client.MongoClient
import com.xxavierr404.crosswave.auth.dao.mongo.MongoUserCredentials
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodecProvider
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoUserCredentialsDaoConfiguration {
    @Bean
    fun mongoClient(
        @Value("\${mongo.connection-string}") connectionString: String,
    ) = MongoClient.create(connectionString)

    @Bean
    fun mongoUserCredentialsCollection(
        mongoClient: MongoClient,
        codecRegistry: CodecRegistry,
    ) =
        mongoClient
            .getDatabase("crosswave")
            .getCollection("userCredentials", MongoUserCredentials::class.java)
            .withCodecRegistry(codecRegistry)

    @Bean
    fun codecRegistry(): CodecRegistry = CodecRegistries.fromProviders(
        UuidCodecProvider(UuidRepresentation.STANDARD),
        MongoClientSettings.getDefaultCodecRegistry()
    )
}