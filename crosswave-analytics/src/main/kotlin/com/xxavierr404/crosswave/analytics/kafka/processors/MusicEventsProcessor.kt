package com.xxavierr404.crosswave.analytics.kafka.processors

import com.xxavierr404.crosswave.kafka.events.model.music.MusicEvent
import org.springframework.kafka.annotation.KafkaListener

open class MusicEventsProcessor(
) {
    @KafkaListener(topics = ["music-events"], groupId = "analytics-group")
    open fun storeMusicEvent(musicEvent: MusicEvent) {

    }
}