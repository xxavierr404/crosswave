package com.xxavierr404.crosswave.analytics.dao

import com.xxavierr404.crosswave.analytics.domain.AnalyticRecord

interface AnalyticRecordsDao {
    fun insertOne(analyticRecord: AnalyticRecord)
}