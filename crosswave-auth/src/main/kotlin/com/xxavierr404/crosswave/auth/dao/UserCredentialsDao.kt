package com.xxavierr404.crosswave.auth.dao

import com.xxavierr404.crosswave.auth.domain.UserCredentials
import java.util.*

interface UserCredentialsDao {
    fun createUser(credentials: UserCredentials)
    fun deleteUser(id: UUID)

    fun getById(id: UUID): UserCredentials?
    fun getByEmail(email: String): UserCredentials?
    fun getByLogin(login: String): UserCredentials?
}