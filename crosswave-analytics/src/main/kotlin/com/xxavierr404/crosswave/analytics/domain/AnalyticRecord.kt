package com.xxavierr404.crosswave.analytics.domain

import java.util.*

data class AnalyticRecord(
    val id: UUID,
    val eventType: String,
    val userId: UUID,
    val trackId: UUID? = null,
    val targetUserId: UUID? = null,
)
