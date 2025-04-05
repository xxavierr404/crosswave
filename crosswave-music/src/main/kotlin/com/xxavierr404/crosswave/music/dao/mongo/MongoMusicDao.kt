package com.xxavierr404.crosswave.music.dao.mongo

import com.mongodb.client.MongoCollection
import com.mongodb.client.gridfs.GridFSBucket
import com.mongodb.client.model.Filters
import com.xxavierr404.crosswave.music.dao.MusicDao
import com.xxavierr404.crosswave.music.domain.MusicInfo
import org.slf4j.LoggerFactory
import java.util.*

class MongoMusicDao(
    private val collection: MongoCollection<MongoMusicInfo>,
    private val musicFilesGridFSBucket: GridFSBucket,
) : MusicDao {
    private val logger = LoggerFactory.getLogger(MongoMusicDao::class.java)

    override fun create(musicInfo: MusicInfo, musicFile: ByteArray) {
        musicFilesGridFSBucket.openUploadStream(musicInfo.id.toString()).use {
            it.write(musicFile)
        }
        collection.insertOne(musicInfo.toMongo())
    }

    override fun getInfo(id: UUID): MusicInfo? {
        return collection.find(
            Filters.eq("_id", id)
        ).firstOrNull()?.toModel()
    }

    override fun getFile(id: UUID, offset: Int, length: Int): ByteArray? {
        val bytes = ByteArray(length + 1)
        try {
            musicFilesGridFSBucket.openDownloadStream(id.toString()).use {
                it.skip(offset.toLong())
                it.readNBytes(bytes, 0, length)
            }
        } catch (exception: Exception) {
            logger.error("Failed to load file $id", exception)
            return null
        }
        return bytes
    }
}