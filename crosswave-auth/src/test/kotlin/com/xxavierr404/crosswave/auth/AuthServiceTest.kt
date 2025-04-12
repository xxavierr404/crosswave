package com.xxavierr404.crosswave.auth

import com.xxavierr404.crosswave.auth.dao.UserCredentialsDao
import com.xxavierr404.crosswave.auth.dao.mongo.MongoUserCredentials
import com.xxavierr404.crosswave.auth.dao.mongo.MongoUserCredentialsDao
import com.xxavierr404.crosswave.auth.service.AuthService
import com.xxavierr404.crosswave.kafka.events.model.JwtService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

private const val TEST_SIGNING_KEY = "6cb484456bccb13b99850c1208f218f66c29baccec6fda7ae612596a40bf6ffbec3280ccc2201992b328139535d29fb51e17fe0fc7aed776e82127c1373fbebe06becf11f55ec1ce13debd030baaa074653a66cc5e2e0942ee9247c43d8e0ac6715c43fc72c02d6415f27582a99ccc951a04e00f97555ccb539fb5022256d670ecb80e008a66822f4f632104afb2cfb0e357503c532d1dc2f918dc071045265581898baca25105ab3da7af30a82eed795baf2209a814813a305dcdd088628bbc8448d7ffd534953fe3d22c4ba739a5256a6db1c4c03204147e8adb392651391f50a8c3234283480fa28c35dc71b47e23ba02c26b6b46cdbc20a50464056b2f6f"

@ExtendWith(MockitoExtension::class)
class AuthServiceTest : E2EBaseClass() {

    private lateinit var authService: AuthService
    private lateinit var dao: UserCredentialsDao

    @BeforeEach
    fun setUp() {
        database.getCollection("userCredentials").drop()
        dao = MongoUserCredentialsDao(database.getCollection("userCredentials", MongoUserCredentials::class.java))
        authService = AuthService(
            dao,
            BCryptPasswordEncoder(),
            JwtService(TEST_SIGNING_KEY, 100000),
            mock()
        )
    }

    @Test
    fun `when create and get user then user returned`() {
        val userLogin = "login"

        authService.register(
            userLogin,
            "a",
            "b"
        )

        val savedUser = dao.getByLogin(userLogin)

        savedUser.shouldNotBeNull()
        savedUser.login shouldBe userLogin
        savedUser.email shouldBe "b"
    }

    @Test
    fun `given already existing user when create then throw`() {
        val userLogin = "login"

        authService.register(
            userLogin,
            "a",
            "b"
        )

        shouldThrow<IllegalArgumentException> {
            authService.register(
                userLogin,
                "c",
                "d"
            )
        }
    }
}