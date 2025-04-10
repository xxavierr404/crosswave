package com.xxavierr404.crosswave.analytics.dao

import com.xxavierr404.crosswave.analytics.domain.UserProfile
import java.util.*

interface UserProfileDao {
    fun createOne(profile: UserProfile)
    fun updateOne(profile: UserProfile)
    fun findOne(userId: UUID): UserProfile?

    fun likeTrack(userId: UUID, trackId: UUID)
    fun unlikeTrack(userId: UUID, trackId: UUID)

    fun subscribe(subscriber: UUID, userId: UUID)
    fun unsubscribe(subscriber: UUID, userId: UUID)
}