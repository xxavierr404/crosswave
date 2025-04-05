package com.xxavierr404.crosswave.auth.configuration

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
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
        @Value("\${crosswave.mongodb.connection-string}") connectionString: String,
    ) = MongoClients.create(connectionString)

    @Bean
    fun mongoUserCredentialsCollection(
        mongoClient: MongoClient,
        codecRegistry: CodecRegistry,
    ): MongoCollection<MongoUserCredentials> =
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