package com.xxavierr404.crosswave.analytics.dao.mongo

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class MongoAnalyticRecord(
    @BsonId
    val id: UUID,
    val eventType: String,
    val userId: UUID,
    val trackId: UUID? = null,
    val targetUserId: UUID? = null,
)
