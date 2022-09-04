package com.urlshorterner.application

import com.urlshorterner.configuration.UrlShorternerProperties
import com.urlshorterner.errorhandling.UrlMapperException
import com.urlshorterner.utils.extractShortUrl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


/**
 * Just a basic test class with the most rudimentary
 */
@ActiveProfiles("unit_test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class ValidUrlShorternerServiceTest {
    @Autowired
    lateinit var service: UrlShorternerService

    @Autowired
    lateinit var properties: UrlShorternerProperties

    @Test
    fun `create short url - happy case`() {
        val longUrl = "https://www.google.com"
        val result = service.create(longUrl)
        assert(result.startsWith(properties.urlPrefix))
        assertEquals(properties.shortUrlLength, result.extractShortUrl().length)
    }

    @ParameterizedTest
    @ValueSource(strings = ["https://www.ea.com", "https://www.dice.se/"])
    fun `look up short url - happy case`(input: String) {
        val shortUrl = service.create(input)
        val result = service.lookup(shortUrl.extractShortUrl())
        assertEquals(input, result)
    }

    @Test
    fun `create short url - testing idempotency`() {
        val firstLongUrl = "https://www.microsoft.com"
        val secondLongUrl = "https://www.apple.com"

        val firstShortUrl = service.create(firstLongUrl)
        val anotherShortUrl = service.create(secondLongUrl)
        val firstShortUrlAgain = service.create(firstLongUrl)

        assertNotEquals(firstShortUrl, anotherShortUrl)
        assertEquals(firstShortUrl, firstShortUrlAgain)
    }

    @Test
    fun `look up short url - missing case`() {
        val message = assertThrows<UrlMapperException> { service.lookup("https://www.nonexistent.com") }.message
        assertEquals("short url not found", message)
    }

    // Add more tests here ...
}
