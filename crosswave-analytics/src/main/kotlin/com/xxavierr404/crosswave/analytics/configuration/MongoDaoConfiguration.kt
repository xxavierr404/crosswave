package com.xxavierr404.crosswave.analytics.configuration

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.xxavierr404.crosswave.analytics.cron.AnalyticsDumpCronTask
import com.xxavierr404.crosswave.analytics.dao.AnalyticRecordsDao
import com.xxavierr404.crosswave.analytics.dao.UserProfileDao
import com.xxavierr404.crosswave.analytics.dao.mongo.MongoAnalyticRecord
import com.xxavierr404.crosswave.analytics.dao.mongo.MongoAnalyticRecordsDao
import com.xxavierr404.crosswave.analytics.dao.mongo.MongoUserProfile
import com.xxavierr404.crosswave.analytics.dao.mongo.MongoUserProfileDao
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodecProvider
import org.bson.codecs.configuration.CodecRegistries
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
class MongoDaoConfiguration {
    @Bean
    fun mongoClient(@Value("\${crosswave.mongodb.connection-string}") connectionString: String) =
        MongoClients.create(
            MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(connectionString))
                .codecRegistry(
                    CodecRegistries.fromProviders(
                        UuidCodecProvider(UuidRepresentation.STANDARD),
                        MongoClientSettings.getDefaultCodecRegistry()
                    )
                )
                .build()
        )

    @Bean
    fun mongoUserProfileCollection(mongoClient: MongoClient): MongoCollection<MongoUserProfile> =
        mongoClient
            .getDatabase("crosswave")
            .getCollection("userProfiles", MongoUserProfile::class.java)

    @Bean
    fun mongoAnalyticsCollection(mongoClient: MongoClient): MongoCollection<MongoAnalyticRecord> =
        mongoClient
            .getDatabase("crosswave")
            .getCollection("analyticRecords", MongoAnalyticRecord::class.java)

    @Bean
    fun mongoUserProfileDao(mongoUserProfileCollection: MongoCollection<MongoUserProfile>): UserProfileDao =
        MongoUserProfileDao(mongoUserProfileCollection)

    @Bean
    fun mongoAnalyticRecordsDao(collection: MongoCollection<MongoAnalyticRecord>) = MongoAnalyticRecordsDao(collection)

    @Bean
    fun analyticsDumpCronTask(analyticRecordsDao: AnalyticRecordsDao) = AnalyticsDumpCronTask(analyticRecordsDao)
}