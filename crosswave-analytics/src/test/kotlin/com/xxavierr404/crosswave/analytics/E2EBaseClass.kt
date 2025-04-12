package com.xxavierr404.crosswave.analytics

import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodecProvider
import org.bson.codecs.configuration.CodecRegistries
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.utility.DockerImageName

abstract class E2EBaseClass {
    companion object {
        @JvmStatic
        protected lateinit var mongoClient: MongoClient
        @JvmStatic
        protected lateinit var database: MongoDatabase

        private val mongoContainer = MongoDBContainer(DockerImageName.parse("mongo:6.0"))

        @BeforeAll
        @JvmStatic
        fun setup() {
            mongoContainer.start()
            mongoClient = MongoClients.create(mongoContainer.connectionString)
            database = mongoClient.getDatabase("crosswave")
                .withCodecRegistry(
                    CodecRegistries.fromProviders(
                        UuidCodecProvider(UuidRepresentation.STANDARD),
                        MongoClientSettings.getDefaultCodecRegistry(),
                    )
                )
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            mongoClient.close()
            mongoContainer.stop()
        }
    }
}