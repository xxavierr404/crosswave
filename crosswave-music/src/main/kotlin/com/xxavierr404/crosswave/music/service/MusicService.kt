package com.xxavierr404.crosswave.music.service

import com.xxavierr404.crosswave.kafka.events.model.music.MusicEvent
import com.xxavierr404.crosswave.kafka.events.model.music.MusicEventType
import com.xxavierr404.crosswave.music.dao.MusicDao
import com.xxavierr404.crosswave.music.domain.MusicInfo
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

class MusicService(
    private val musicDao: MusicDao,
    private val kafkaTemplate: KafkaTemplate<UUID, MusicEvent>,
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

    fun uploadTrack(musicInfo: MusicInfo, fileBytes: ByteArray) {
        musicDao.create(musicInfo, fileBytes)
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