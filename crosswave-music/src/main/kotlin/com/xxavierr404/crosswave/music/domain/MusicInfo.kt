package com.xxavierr404.crosswave.music.domain

import java.util.*

data class MusicInfo(
    val id: UUID,
    val uploaderId: UUID,
    val author: String,
    val name: String,
    val fileId: String? = null,
)
