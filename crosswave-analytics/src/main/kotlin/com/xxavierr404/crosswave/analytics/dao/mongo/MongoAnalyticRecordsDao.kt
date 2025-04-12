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

    override fun findAll(): List<AnalyticRecord> = collection.find().map { it.toModel() }.toList()
}

private fun AnalyticRecord.toMongo() = MongoAnalyticRecord(
    id,
    eventType,
    userId,
    trackId,
    targetUserId,
)

private fun MongoAnalyticRecord.toModel() = AnalyticRecord(
    id,
    eventType,
    userId,
    trackId,
    targetUserId,
)
