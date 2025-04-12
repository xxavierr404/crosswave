package com.xxavierr404.crosswave.analytics.service

import com.xxavierr404.crosswave.analytics.dao.UserProfileDao
import com.xxavierr404.crosswave.analytics.domain.UserProfile
import com.xxavierr404.crosswave.kafka.events.model.music.MusicEvent
import com.xxavierr404.crosswave.kafka.events.model.music.MusicEventType
import com.xxavierr404.crosswave.kafka.events.model.social.SocialEvent
import com.xxavierr404.crosswave.kafka.events.model.social.SocialEventType
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

class UserProfileService(
    private val userProfileDao: UserProfileDao,
    private val musicKafka: KafkaTemplate<UUID, MusicEvent>,
    private val socialKafka: KafkaTemplate<UUID, SocialEvent>,
) {
    fun findProfile(userId: UUID) = userProfileDao.findOne(userId)

    fun updateProfile(userProfile: UserProfile) = userProfileDao.updateOne(userProfile)

    fun likeTrack(userId: UUID, trackId: UUID) {
        userProfileDao.likeTrack(userId, trackId)
        musicKafka.send(
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
        musicKafka.send(
            "music-events",
            UUID.randomUUID(),
            MusicEvent(
                userId,
                MusicEventType.DISLIKE,
                trackId
            )
        )
    }

    fun subscribe(subscriber: UUID, userId: UUID) {
        if (subscriber == userId) {
            throw IllegalArgumentException("Can't subscribe to self")
        }
        userProfileDao.subscribe(subscriber, userId)
        socialKafka.send(
            "profile-events",
            UUID.randomUUID(),
            SocialEvent(
                SocialEventType.SUBSCRIBE,
                subscriber,
                userId
            )
        )
    }

    fun unsubscribe(subscriber: UUID, userId: UUID) {
        if (subscriber == userId) {
            throw IllegalArgumentException("Can't unsubscribe from self")
        }
        userProfileDao.unsubscribe(subscriber, userId)
        socialKafka.send(
            "profile-events",
            UUID.randomUUID(),
            SocialEvent(
                SocialEventType.UNSUBSCRIBE,
                subscriber,
                userId
            )
        )
    }
}
