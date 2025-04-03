package com.xxavierr404.crosswave.music.configuration

import org.springframework.context.annotation.Import
import org.xxavierr404.auth.starter.CrosswaveAuthConfiguration

@Import(
    CrosswaveAuthConfiguration::class
)
class MusicAppConfiguration {
}