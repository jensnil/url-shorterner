package com.urlshorterner.rest

import com.urlshorterner.errorhandling.ErrorResponse
import com.urlshorterner.infrastructure.UrlShorternerController
import com.urlshorterner.utils.extractShortUrl
import com.urlshorterner.utils.getUrl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.servlet.ModelAndView
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@Suppress("UnnecessaryAbstractClass")
@ActiveProfiles("integration_test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidUrlShorternerControllerIntegrationTest {
    @Autowired
    lateinit var controller: UrlShorternerController

    @LocalServerPort
    private var port = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `context loads`() {
        assertNotNull(controller)
    }

    @Test
    fun `create - happy case`() {
        val response = restTemplate.postForEntity(
            getUrl(port),
            HttpEntity("https://www.google.com"),
            String::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `create - incorrect url`() {
        val response = restTemplate.postForEntity(
            getUrl(port),
            HttpEntity("this is not a correct url"),
            String::class.java)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }

    @Test
    fun `lookup - happy case`() {
        val url = "https://www.google.com"
        val shortUrl = restTemplate.postForEntity(
            getUrl(port),
            HttpEntity(url),
            String::class.java).body!!.extractShortUrl()
        val response = restTemplate.getForEntity(
            "${getUrl(port)}$shortUrl",
            ModelAndView::class.java)
        assertEquals(302, response.statusCode.value())
        assertEquals(url, response.headers["Location"]?.first())
    }

    @Test
    fun `lookup - missing url`() {
        val url = "https://www.google.com"
        val shortUrl = "missing"
        val response = restTemplate.getForEntity(
            "${getUrl(port)}$shortUrl",
            Any::class.java)
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}
