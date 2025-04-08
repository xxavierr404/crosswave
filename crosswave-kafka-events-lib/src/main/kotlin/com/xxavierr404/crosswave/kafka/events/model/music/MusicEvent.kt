package com.xxavierr404.crosswave.kafka.events.model.music

import java.util.*

data class MusicEvent(
    val userId: UUID,
    val eventType: MusicEventType,
    val trackId: UUID,
)
