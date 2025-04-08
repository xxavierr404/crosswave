package com.xxavierr404.crosswave.kafka.events.model

import com.xxavierr404.crosswave.auth.domain.UserCredentials
import io.jsonwebtoken.Jwts
import java.util.*
import javax.crypto.spec.SecretKeySpec

class JwtService(
    private val jwtSigningKey: String,
    private val jwtExpiration: Long,
) {
    fun generateToken(credentials: UserCredentials): String {
        val key = SecretKeySpec(jwtSigningKey.toByteArray(), "HmacSHA256")
        return Jwts.builder()
           .subject(credentials.id.toString())
           .issuedAt(Date())
           .expiration(Date(System.currentTimeMillis() + jwtExpiration))
           .signWith(key)
           .compact()
    }

    fun parseToken(token: String): UUID {
        val key = SecretKeySpec(jwtSigningKey.toByteArray(), "HmacSHA256")
        return UUID.fromString(
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload.subject
        )
    }
}