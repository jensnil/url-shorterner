package com.urlshorterner.application

import com.urlshorterner.configuration.UrlShorternerProperties
import com.urlshorterner.domain.UrlMapper
import com.urlshorterner.domain.UrlMapperRepository
import com.urlshorterner.errorhandling.UrlMapperException
import net.bytebuddy.utility.RandomString
import org.apache.commons.validator.routines.UrlValidator
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import java.time.LocalDateTime

@Service
@Validated
class UrlShorternerServiceImpl(
    private val urlMapperRepository: UrlMapperRepository,
    private val urlShorternerProperties: UrlShorternerProperties
): UrlShorternerService {

    @Transactional
    override fun create(longUrl: String): String {
        validateUrl(longUrl)
        val urlMapper = urlMapperRepository.findByLongUrl(longUrl)
            ?: run {
                val shortUrl = generateShortUrl()
                save(UrlMapper(longUrl = longUrl, shortUrl = shortUrl, created = LocalDateTime.now()))
            }
        return urlShorternerProperties.urlPrefix + urlMapper.shortUrl
    }

    override fun lookup(shortUrl: String): String {
        // For faster lookup, consider a CQRS-like architecture, with dedicated replicated database(s)
        // specialized for fast querying
        val urlMapper = urlMapperRepository.findByShortUrl(shortUrl)
        return urlMapper?.longUrl ?: throw UrlMapperException("short url not found")
    }

    /**
     * Look for increasingly longer short urls
     */
    private fun generateShortUrl() : String {
        (urlShorternerProperties.shortUrlLength until UrlShorternerProperties.MAX_LONG_URL_LENGTH).forEach { length ->
            val shortUrl = RandomString(length).nextString()
            if (urlMapperRepository.findByShortUrl(shortUrl) == null) {
                return shortUrl
            }
        }
        // Extremely totally unlikely
        throw UrlMapperException("Oh, we ran out of short urls")
    }

    private fun save(urlMapper: UrlMapper): UrlMapper {
        return try {
            urlMapperRepository.saveAndFlush(urlMapper)
        } catch (e: DataIntegrityViolationException) {
            // In case same long url is written at the same time by a different thread within the same instance
            // or by another instance.
            urlMapperRepository.findByShortUrl(shortUrl = urlMapper.shortUrl) ?:
                throw UrlMapperException("Database is not feeling well")
        }
    }

    private fun validateUrl(longUrl: String) {
        // Use parameter annotation validation instead
        if (!UrlValidator(arrayOf("http","https")).isValid(longUrl)) {
            throw UrlMapperException("Invalid url format: $longUrl")
        }
    }
}
