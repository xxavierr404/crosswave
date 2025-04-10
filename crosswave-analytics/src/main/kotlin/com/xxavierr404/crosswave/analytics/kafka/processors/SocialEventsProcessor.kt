package com.xxavierr404.crosswave.analytics.kafka.processors

import com.xxavierr404.crosswave.analytics.dao.AnalyticRecordsDao
import com.xxavierr404.crosswave.analytics.domain.AnalyticRecord
import com.xxavierr404.crosswave.kafka.events.model.social.SocialEvent
import org.springframework.kafka.annotation.KafkaListener
import java.util.*

open class SocialEventsProcessor(
    private val analyticRecordsDao: AnalyticRecordsDao,
) {
    @KafkaListener(
        topics = ["social-events"],
        groupId = "analytics-group",
        containerFactory = "socialEventKafkaListenerContainerFactory"
    )
    open fun storeMusicEvent(socialEvent: SocialEvent) {
        analyticRecordsDao.insertOne(
            AnalyticRecord(
                id = UUID.randomUUID(),
                eventType = socialEvent.type.name,
                userId = socialEvent.userId,
                targetUserId = socialEvent.targetUserId
            )
        )
    }
}