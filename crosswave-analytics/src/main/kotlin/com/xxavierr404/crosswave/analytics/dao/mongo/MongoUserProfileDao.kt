package com.xxavierr404.crosswave.analytics.dao.mongo

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.xxavierr404.crosswave.analytics.dao.UserProfileDao
import com.xxavierr404.crosswave.analytics.domain.UserProfile
import java.util.*

class MongoUserProfileDao(
    private val collection: MongoCollection<MongoUserProfile>
) : UserProfileDao {
    override fun createOne(profile: UserProfile) {
        collection.insertOne(profile.toMongo())
    }

    override fun updateOne(profile: UserProfile) {
        collection.replaceOne(
            Filters.eq("_id", profile.id),
            profile.toMongo()
        )
    }

    override fun findOne(userId: UUID): UserProfile? {
        return collection.find(
            Filters.eq("_id", userId)
        ).firstOrNull()?.toModel()
    }

    override fun likeTrack(userId: UUID, trackId: UUID) {
        collection.updateOne(
            Filters.eq("_id", userId),
            Updates.addToSet("likedTracks", trackId)
        )
    }

    override fun unlikeTrack(userId: UUID, trackId: UUID) {
        collection.updateOne(
            Filters.eq("_id", userId),
            Updates.pull("likedTracks", trackId)
        )
    }
}
