package com.xxavierr404.crosswave.analytics.domain

import java.util.*

data class UserProfile(
    val id: UUID,
    val name: String,
    val surname: String,
    val bio: String,
    val likedTracks: List<UUID>,
)
