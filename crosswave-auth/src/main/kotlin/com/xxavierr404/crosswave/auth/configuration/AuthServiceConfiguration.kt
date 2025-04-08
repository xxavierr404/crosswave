package com.xxavierr404.crosswave.auth.configuration

import com.xxavierr404.crosswave.kafka.events.model.JwtService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AuthServiceConfiguration {
    @Bean
    fun jwtService(
        @Value("\${jwt.secret}") jwtSigningKey: String,
        @Value("\${jwt.expiration.millis}") jwtExpiration: Long,
    ) = JwtService(jwtSigningKey, jwtExpiration)

    @Bean
    fun passwordEncoder(): PasswordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8()
}