package com.xxavierr404.crosswave.auth.dao.mongo

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.MongoCollection
import com.xxavierr404.crosswave.auth.dao.UserCredentialsDao
import com.xxavierr404.crosswave.auth.domain.UserCredentials
import org.springframework.stereotype.Component
import java.util.*

@Component
class MongoUserCredentialsDao(
    private val collection: MongoCollection<MongoUserCredentials>
) : UserCredentialsDao {

    override fun createUser(credentials: UserCredentials) {
        collection.insertOne(credentials.toMongo())
    }

    override fun getById(id: UUID): UserCredentials? =
        collection.find(Filters.eq("_id", id)).firstOrNull()?.toModel()

    override fun deleteUser(id: UUID) {
        collection.deleteOne(Filters.eq("_id", id))
    }

    override fun getByEmail(email: String): UserCredentials? =
        collection.find(Filters.eq("email", email)).firstOrNull()?.toModel()

    override fun getByLogin(login: String): UserCredentials? =
        collection.find(Filters.eq("login", login)).firstOrNull()?.toModel()
}

fun UserCredentials.toMongo(): MongoUserCredentials = MongoUserCredentials(
    id = id,
    login = login,
    email = email,
    passwordHash = passwordHash,
)

fun MongoUserCredentials.toModel(): UserCredentials = UserCredentials(
    id = id,
    login = login,
    email = email,
    passwordHash = passwordHash,
)
