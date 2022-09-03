package com.urlshorterner.application

import com.urlshorterner.domain.UrlMapperRepository
import org.springframework.stereotype.Service

interface UrlShorternerService {

    /**
     * Takes a url and return a short url
     *
     * @param longUrl the long url
     *
     * @exception UrlMapperException, if longUrl is not a valid url
     *
     */
    fun create(longUrl: String): String

    /**
     * Takes a short url and return the long url
     *
     * @param longUrl the long url
     *
     * @exception UrlMapperException, if shortUrl does not exit
     *
     */
    fun lookup(shortUrl: String): String
}
