package com.xxavierr404.crosswave.analytics.kafka.processors

import com.xxavierr404.crosswave.analytics.dao.UserProfileDao
import com.xxavierr404.crosswave.analytics.domain.UserProfile
import com.xxavierr404.crosswave.kafka.events.model.auth.AuthEvent
import com.xxavierr404.crosswave.kafka.events.model.auth.AuthEventType
import org.springframework.kafka.annotation.KafkaListener

open class AuthEventsProcessor(
    private val userProfileDao: UserProfileDao,
) {
    @KafkaListener(
        topics = ["auth-events"],
        groupId = "auth-group",
        containerFactory = "authEventListenerContainerFactory"
    )
    open fun registerNewUser(authEvent: AuthEvent) {
        if (authEvent.type == AuthEventType.REGISTER) {
            userProfileDao.createOne(UserProfile(
                authEvent.userId,
                "Crosswave",
                "User",
                "Sample bio"
            ))
        }
    }
}