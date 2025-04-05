package com.xxavierr404.crosswave.music.controller

import com.xxavierr404.crosswave.music.domain.MusicInfo
import com.xxavierr404.crosswave.music.service.MusicService
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import org.xxavierr404.crosswave.music.api.apis.MusicApiController
import org.xxavierr404.crosswave.music.api.models.TrackInfo
import java.util.*

@RestController
class MusicController(
    private val musicService: MusicService,
): MusicApiController() {

    override fun download(xUserId: String, trackId: String): ResponseEntity<Resource> {
        val fileResource = musicService.downloadTrack(UUID.fromString(trackId))?.let { ByteArrayResource(it) }

        return fileResource?.let { ResponseEntity.ok(fileResource) }
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    override fun getInfo(xUserId: String, trackId: String): ResponseEntity<TrackInfo> {
        return musicService.readInfo(UUID.fromString(trackId))?.let { ResponseEntity.ok(it.toDto()) }
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    override fun upload(xUserId: String, author: String, name: String, body: MultipartFile): ResponseEntity<TrackInfo> {
        val trackId = UUID.randomUUID()
        musicService.uploadTrack(
            MusicInfo(
                trackId,
                UUID.fromString(xUserId),
                author,
                name,
            ),
            body.bytes
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(musicService.readInfo(trackId)!!.toDto())
    }
}

private fun MusicInfo.toDto(): TrackInfo = TrackInfo(
    id,
    uploaderId,
    author,
    name,
)
