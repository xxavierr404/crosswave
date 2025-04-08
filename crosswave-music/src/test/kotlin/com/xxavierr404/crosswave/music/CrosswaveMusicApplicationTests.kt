package com.xxavierr404.crosswave.music

import org.junit.jupiter.api.Test
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@EnableConfigurationProperties
@ActiveProfiles("tests")
class CrosswaveMusicApplicationTests {

	@Test
	fun contextLoads() {
	}

}
