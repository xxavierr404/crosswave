package com.xxavierr404.crosswave.auth.service

import com.xxavierr404.crosswave.auth.dao.UserCredentialsDao
import com.xxavierr404.crosswave.auth.domain.UserCredentials
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val userCredentialsDao: UserCredentialsDao,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
) {
    fun register(login: String, password: String, email: String): UserCredentials {
        if (userCredentialsDao.getByLogin(login) != null) {
            throw IllegalArgumentException("User with this login already exists")
        }

        if (userCredentialsDao.getByEmail(email) != null) {
            throw IllegalArgumentException("User with this email already exists")
        }

        val id = UUID.randomUUID()
        userCredentialsDao.createUser(
            UserCredentials(
                id = id,
                login = login,
                email = email,
                passwordHash = passwordEncoder.encode(password),
            )
        )
        return userCredentialsDao.getById(id)!!
    }

    fun login(login: String, password: String): String {
        val user = userCredentialsDao.getByLogin(login)

        if (user == null || !passwordEncoder.matches(password, user.passwordHash)) {
            throw IllegalArgumentException("Invalid login or password")
        }

        return jwtService.generateToken(user)
    }

    fun resolveUUIDByToken(token: String): UUID {
        return jwtService.parseToken(token)
    }
}