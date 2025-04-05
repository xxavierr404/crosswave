package com.xxavierr404.crosswave.music.dao

import com.xxavierr404.crosswave.music.domain.MusicInfo
import java.util.*

interface MusicDao {
    fun create(musicInfo: MusicInfo, musicFile: ByteArray)
    fun getInfo(id: UUID): MusicInfo?
    fun getFile(id: UUID, offset: Int, length: Int): ByteArray?
}