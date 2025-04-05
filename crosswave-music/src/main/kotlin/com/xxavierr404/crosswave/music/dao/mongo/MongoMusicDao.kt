package com.xxavierr404.crosswave.music.dao.mongo

import com.mongodb.client.MongoCollection
import com.mongodb.client.gridfs.GridFSBucket
import com.mongodb.client.model.Filters
import com.xxavierr404.crosswave.music.dao.MusicDao
import com.xxavierr404.crosswave.music.domain.MusicInfo
import java.util.*

class MongoMusicDao(
    private val collection: MongoCollection<MongoMusicInfo>,
    private val musicFilesGridFSBucket: GridFSBucket,
) : MusicDao {
    override fun create(musicInfo: MusicInfo, musicFile: ByteArray) {
        musicFilesGridFSBucket.openUploadStream(musicInfo.id.toString()).write(musicFile)
        collection.insertOne(musicInfo.toMongo())
    }

    override fun getInfo(id: UUID): MusicInfo? {
        return collection.find(
            Filters.eq("_id", id)
        ).firstOrNull()?.toModel()
    }

    override fun getFile(id: UUID): ByteArray? {
        if (musicFilesGridFSBucket.find(Filters.eq("_id", id)).none()) {
            return null
        }
        return musicFilesGridFSBucket.openDownloadStream(id.toString()).readAllBytes()
    }
}