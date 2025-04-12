package com.xxavierr404.crosswave.analytics

import com.xxavierr404.crosswave.analytics.dao.UserProfileDao
import com.xxavierr404.crosswave.analytics.dao.mongo.MongoUserProfile
import com.xxavierr404.crosswave.analytics.dao.mongo.MongoUserProfileDao
import com.xxavierr404.crosswave.analytics.domain.UserProfile
import com.xxavierr404.crosswave.analytics.service.UserProfileService
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import java.util.*

class ProfileServiceTest : E2EBaseClass() {
    private lateinit var profileService: UserProfileService
    private lateinit var dao: UserProfileDao

    @BeforeEach
    fun setUp() {
        database.getCollection("userProfiles").drop()
        dao = MongoUserProfileDao(database.getCollection("userProfiles", MongoUserProfile::class.java))
        profileService = UserProfileService(
            dao,
            mock(),
            mock()
        )
    }

    @Test
    fun `when update and get user then user updated`() {
        val userUuid = UUID.randomUUID()

        dao.createOne(UserProfile(
            userUuid,
            "name",
            "surname",
            "afafa"
        ))

        profileService.updateProfile(
            UserProfile(
                userUuid,
                "noname",
                "nosurname",
                "amogus"
            )
        )

        val savedUser = profileService.findProfile(userUuid)

        savedUser.shouldNotBeNull()
        savedUser shouldBe UserProfile(
            userUuid,
            "noname",
            "nosurname",
            "amogus"
        )
    }

    @Test
    fun `when subscribe and get user then user updated`() {
        val userUuid = UUID.randomUUID()
        val targetUserUuid = UUID.randomUUID()

        dao.createOne(UserProfile(
            userUuid,
            "name",
            "surname",
            "afafa"
        ))

        profileService.subscribe(
            userUuid,
            targetUserUuid
        )

        val savedUser = profileService.findProfile(userUuid)

        savedUser.shouldNotBeNull()
        savedUser.subscriptions shouldBe setOf(targetUserUuid)
    }

    @Test
    fun `when subscribe and unsubscribe and get user then user updated`() {
        val userUuid = UUID.randomUUID()
        val targetUserUuid = UUID.randomUUID()

        dao.createOne(UserProfile(
            userUuid,
            "name",
            "surname",
            "afafa"
        ))

        profileService.subscribe(
            userUuid,
            targetUserUuid
        )

        profileService.unsubscribe(
            userUuid,
            targetUserUuid
        )

        val savedUser = profileService.findProfile(userUuid)

        savedUser.shouldNotBeNull()
        savedUser.subscriptions shouldBe setOf()
    }

    @Test
    fun `when subscribe to self then throw`() {
        val userUuid = UUID.randomUUID()

        dao.createOne(UserProfile(
            userUuid,
            "name",
            "surname",
            "afafa"
        ))

        assertThrows<IllegalArgumentException> {
            profileService.subscribe(
                userUuid,
                userUuid
            )
        }
    }
}