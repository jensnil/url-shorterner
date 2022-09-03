package com.urlshorterner.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "url-shorterner")
@Configuration
class UrlShorternerProperties {
    var shortUrlLength : Int = 8
    lateinit var urlPrefix : String

    companion object {
        const val MAX_SHORT_URL_LENGTH = 50
        const val MAX_LONG_URL_LENGTH = 2048
    }
}
