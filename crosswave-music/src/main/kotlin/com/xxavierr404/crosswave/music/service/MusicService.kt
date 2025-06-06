package com.xxavierr404.crosswave.music.service

import com.xxavierr404.crosswave.kafka.events.model.music.MusicEvent
import com.xxavierr404.crosswave.kafka.events.model.music.MusicEventType
import com.xxavierr404.crosswave.music.dao.MusicDao
import com.xxavierr404.crosswave.music.domain.MusicInfo
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.xxavierr404.crosswave.ai.client.apis.DefaultApi
import java.util.*

private val LOGGER = LoggerFactory.getLogger(MusicService::class.java)

class MusicService(
    private val musicDao: MusicDao,
    private val kafkaTemplate: KafkaTemplate<UUID, MusicEvent>,
    private val aiClient: DefaultApi,
) {
    fun readInfo(userId: UUID, trackId: UUID): MusicInfo? {
        kafkaTemplate.send(
            "music-events",
            UUID.randomUUID(),
            MusicEvent(
                userId,
                MusicEventType.VIEW,
                trackId
            )
        )
        return musicDao.getInfo(trackId)
    }

    fun uploadTrack(
        trackId: UUID,
        userId: UUID,
        author: String,
        name: String,
        fileBytes: ByteArray
    ) {
        val genreResponse = kotlin.runCatching {
            aiClient.predictGenre(fileBytes).block()!!
        }

        if (genreResponse.isFailure) {
            LOGGER.error("Failed to determine track genre automatically", genreResponse.exceptionOrNull())
        }

        musicDao.create(
            MusicInfo(
                trackId,
                userId,
                author,
                name,
                genreResponse.getOrNull()?.genre ?: "unknown"
            ),
            fileBytes
        )
    }

    fun streamTrack(userId: UUID, trackId: UUID, offset: Int, length: Int): ByteArray? {
        kafkaTemplate.send(
            "music-events",
            UUID.randomUUID(),
            MusicEvent(
                userId,
                MusicEventType.LISTEN,
                trackId
            )
        )
        return musicDao.getFile(trackId, offset, length)
    }
}