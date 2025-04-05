package com.xxavierr404.crosswave.music.configuration

import com.xxavierr404.crosswave.music.dao.MusicDao
import com.xxavierr404.crosswave.music.service.MusicService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MusicServiceConfiguration {
    @Bean
    fun musicService(musicDao: MusicDao) = MusicService(musicDao)
}