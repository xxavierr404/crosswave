package com.xxavierr404.crosswave.analytics.cron

import com.xxavierr404.crosswave.analytics.dao.AnalyticRecordsDao
import org.springframework.scheduling.annotation.Scheduled
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardOpenOption

class AnalyticsDumpCronTask(
    private val analyticRecordsDao: AnalyticRecordsDao,
) {
    @Scheduled(fixedDelay = 3000)
    fun dumpAnalytics() {
        val file = File("dumps/analytics.csv")
        file.parentFile.mkdir()
        file.createNewFile()

        val records = analyticRecordsDao.findAll().map {
            "${it.id},${it.eventType},${it.userId},${it.trackId},${it.targetUserId}"
        }
        Files.write(
            file.toPath(),
            "EVENT_ID,EVENT_TYPE,USER_ID,TRACK_ID,TARGET_USER_ID\n".encodeToByteArray(),
            StandardOpenOption.TRUNCATE_EXISTING
        )
        Files.write(file.toPath(), records, StandardOpenOption.APPEND)
    }
}