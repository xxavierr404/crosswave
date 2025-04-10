package com.xxavierr404.crosswave.analytics.dao.mongo

import com.mongodb.client.MongoCollection
import com.xxavierr404.crosswave.analytics.dao.AnalyticRecordsDao
import com.xxavierr404.crosswave.analytics.domain.AnalyticRecord

class MongoAnalyticRecordsDao(
    private val collection: MongoCollection<MongoAnalyticRecord>,
) : AnalyticRecordsDao {
    override fun insertOne(analyticRecord: AnalyticRecord) {
        collection.insertOne(analyticRecord.toMongo())
    }
}

private fun AnalyticRecord.toMongo() = MongoAnalyticRecord(
    id,
    eventType,
    userId,
    trackId,
    targetUserId,
)
