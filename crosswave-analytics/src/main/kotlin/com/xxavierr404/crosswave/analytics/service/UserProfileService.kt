package com.xxavierr404.crosswave.analytics.service

import com.xxavierr404.crosswave.analytics.dao.UserProfileDao
import com.xxavierr404.crosswave.analytics.domain.UserProfile
import com.xxavierr404.crosswave.kafka.events.model.music.MusicEvent
import com.xxavierr404.crosswave.kafka.events.model.music.MusicEventType
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

class UserProfileService(
    private val userProfileDao: UserProfileDao,
    private val kafka: KafkaTemplate<UUID, MusicEvent>,
) {
    fun findProfile(userId: UUID) = userProfileDao.findOne(userId)

    fun updateProfile(userProfile: UserProfile) = userProfileDao.updateOne(userProfile)

    fun likeTrack(userId: UUID, trackId: UUID) {
        userProfileDao.likeTrack(userId, trackId)
        kafka.send(
            "music-events",
            UUID.randomUUID(),
            MusicEvent(
                userId,
                MusicEventType.LIKE,
                trackId
            )
        )
    }

    fun dislikeTrack(userId: UUID, trackId: UUID) {
        userProfileDao.unlikeTrack(userId, trackId)
        kafka.send(
            "music-events",
            UUID.randomUUID(),
            MusicEvent(
                userId,
                MusicEventType.DISLIKE,
                trackId
            )
        )
    }
}