package com.xxavierr404.crosswave.kafka.events.model.social

import java.util.*

data class SocialEvent(
    val type: SocialEventType,
    val userId: UUID,
    val targetUserId: UUID?,
)
