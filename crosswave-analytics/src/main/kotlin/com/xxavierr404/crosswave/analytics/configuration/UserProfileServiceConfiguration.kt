package com.xxavierr404.crosswave.analytics.configuration

import com.xxavierr404.crosswave.analytics.dao.UserProfileDao
import com.xxavierr404.crosswave.analytics.service.UserProfileService
import com.xxavierr404.crosswave.kafka.events.model.music.MusicEvent
import com.xxavierr404.crosswave.kafka.events.model.social.SocialEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.KafkaTemplate
import java.util.*

@Configuration
@Import(
    MongoDaoConfiguration::class,
    KafkaConfiguration::class,
)
class UserProfileServiceConfiguration {
    @Bean
    fun userProfileService(
        userProfileDao: UserProfileDao,
        musicKafka: KafkaTemplate<UUID, MusicEvent>,
        socialKafka: KafkaTemplate<UUID, SocialEvent>,
    ) = UserProfileService(userProfileDao, musicKafka, socialKafka)
}