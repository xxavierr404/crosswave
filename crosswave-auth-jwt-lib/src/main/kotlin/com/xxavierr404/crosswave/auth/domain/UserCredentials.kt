package com.xxavierr404.crosswave.auth.domain

import java.util.*

data class UserCredentials(
    val id: UUID,
    val email: String,
    val login: String,
    val passwordHash: String,
)
