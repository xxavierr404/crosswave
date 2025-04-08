package com.xxavierr404.crosswave.analytics.kafka.processors

import com.xxavierr404.crosswave.analytics.dao.AnalyticRecordsDao
import com.xxavierr404.crosswave.analytics.domain.AnalyticRecord
import com.xxavierr404.crosswave.kafka.events.model.music.MusicEvent
import org.springframework.kafka.annotation.KafkaListener
import java.util.*

open class MusicEventsProcessor(
    private val analyticRecordsDao: AnalyticRecordsDao
) {
    @KafkaListener(
        topics = ["music-events"],
        groupId = "analytics-group",
        containerFactory = "musicEventKafkaListenerContainerFactory"
    )
    open fun storeMusicEvent(musicEvent: MusicEvent) {
        analyticRecordsDao.insertOne(
            AnalyticRecord(
                id = UUID.randomUUID(),
                eventType = musicEvent.eventType.name,
                userId = musicEvent.userId,
                trackId = musicEvent.trackId,
            )
        )
    }
}