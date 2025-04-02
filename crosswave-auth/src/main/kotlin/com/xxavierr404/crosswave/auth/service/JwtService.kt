package com.xxavierr404.crosswave.auth.service

import com.xxavierr404.crosswave.auth.domain.UserCredentials
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.spec.SecretKeySpec

@Service
class JwtService(
    @Value("\${jwt.secret}") private val jwtSigningKey: String,
    @Value("\${jwt.expiration.millis}") private val jwtExpiration: Long,
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