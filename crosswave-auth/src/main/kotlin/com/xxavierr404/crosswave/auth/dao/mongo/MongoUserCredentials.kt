package com.xxavierr404.crosswave.auth.dao.mongo

import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class MongoUserCredentials(
    @BsonId
    val id: UUID,
    val email: String,
    val login: String,
    val passwordHash: String,
)
