package com.xxavierr404.crosswave.jwt.service

import com.xxavierr404.crosswave.auth.domain.UserCredentials
import com.xxavierr404.crosswave.kafka.events.model.JwtService
import io.jsonwebtoken.JwtException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.*

private const val TEST_SIGNING_KEY = "6cb484456bccb13b99850c1208f218f66c29baccec6fda7ae612596a40bf6ffbec3280ccc2201992b328139535d29fb51e17fe0fc7aed776e82127c1373fbebe06becf11f55ec1ce13debd030baaa074653a66cc5e2e0942ee9247c43d8e0ac6715c43fc72c02d6415f27582a99ccc951a04e00f97555ccb539fb5022256d670ecb80e008a66822f4f632104afb2cfb0e357503c532d1dc2f918dc071045265581898baca25105ab3da7af30a82eed795baf2209a814813a305dcdd088628bbc8448d7ffd534953fe3d22c4ba739a5256a6db1c4c03204147e8adb392651391f50a8c3234283480fa28c35dc71b47e23ba02c26b6b46cdbc20a50464056b2f6f"

class JwtServiceTest {
    private val jwtService = JwtService(TEST_SIGNING_KEY, 100000)

    @Test
    fun `when encode and decode jwt then data ok`() {
        val credentials = UserCredentials(
            UUID.randomUUID(),
            "aaa",
            "bbb",
            "ccc"
        )

        val token = jwtService.generateToken(credentials)
        val userData = jwtService.parseToken(token)

        userData shouldBe credentials.id
    }

    @Test
    fun `when decode broken jwt then throw`() {
        shouldThrow<JwtException> {
            jwtService.parseToken("INVALID TOKEN!!!")
        }
    }
}