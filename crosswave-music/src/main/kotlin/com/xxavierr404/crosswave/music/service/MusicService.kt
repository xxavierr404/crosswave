package com.xxavierr404.crosswave.music.service

import com.xxavierr404.crosswave.music.dao.MusicDao
import com.xxavierr404.crosswave.music.domain.MusicInfo
import java.util.*

class MusicService(
    private val musicDao: MusicDao,
) {
    fun readInfo(trackId: UUID): MusicInfo? {
        return musicDao.getInfo(trackId)
    }

    fun uploadTrack(musicInfo: MusicInfo, fileBytes: ByteArray) {
        musicDao.create(musicInfo, fileBytes)
    }

    fun downloadTrack(trackId: UUID): ByteArray? {
        return musicDao.getFile(trackId)
    }
}