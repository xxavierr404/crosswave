package com.xxavierr404.crosswave.kafka.events.model.auth

import java.util.*

data class AuthEvent(
    val type: AuthEventType,
    val userId: UUID
)
