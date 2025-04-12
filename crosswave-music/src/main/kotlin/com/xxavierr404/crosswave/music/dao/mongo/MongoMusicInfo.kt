package com.xxavierr404.crosswave.music.dao.mongo

import com.xxavierr404.crosswave.music.domain.MusicInfo
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class MongoMusicInfo(
    @BsonId
    val id: UUID,
    val uploaderId: UUID,
    val author: String,
    val name: String,
    val genre: String
)

fun MongoMusicInfo.toModel() = MusicInfo(
    id,
    uploaderId,
    author,
    name,
    genre
)

fun MusicInfo.toMongo() = MongoMusicInfo(
    id,
    uploaderId,
    author,
    name,
    genre
)
