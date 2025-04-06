package com.xxavierr404.crosswave.analytics.dao.mongo

import com.xxavierr404.crosswave.analytics.domain.UserProfile
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class MongoUserProfile(
    @BsonId
    val id: UUID,
    val name: String,
    val surname: String,
    val bio: String,
    val likedTracks: List<UUID>,
)

fun MongoUserProfile.toModel() = UserProfile(
    id,
    name,
    surname,
    bio,
    likedTracks,
)

fun UserProfile.toMongo() = MongoUserProfile(
    id,
    name,
    surname,
    bio,
    likedTracks,
)
