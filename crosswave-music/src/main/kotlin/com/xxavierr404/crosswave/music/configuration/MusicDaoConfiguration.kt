package com.xxavierr404.crosswave.music.configuration

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.gridfs.GridFSBucket
import com.mongodb.client.gridfs.GridFSBuckets
import com.xxavierr404.crosswave.music.dao.MusicDao
import com.xxavierr404.crosswave.music.dao.mongo.MongoMusicDao
import com.xxavierr404.crosswave.music.dao.mongo.MongoMusicInfo
import org.bson.UuidRepresentation
import org.bson.codecs.UuidCodecProvider
import org.bson.codecs.configuration.CodecRegistries
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MusicDaoConfiguration {
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
    fun mongoMusicInfoCollection(mongoClient: MongoClient): MongoCollection<MongoMusicInfo> =
        mongoClient
            .getDatabase("crosswave")
            .getCollection("musicInfo", MongoMusicInfo::class.java)

    @Bean
    fun mongoMusicDao(
        mongoCollection: MongoCollection<MongoMusicInfo>,
        musicFilesGridFSBucket: GridFSBucket,
    ): MusicDao =
        MongoMusicDao(mongoCollection, musicFilesGridFSBucket)

    @Bean
    fun musicFilesGrifdsBucket(mongoClient: MongoClient): GridFSBucket = GridFSBuckets.create(
        mongoClient.getDatabase("crosswave"), "musicFiles"
    )
}