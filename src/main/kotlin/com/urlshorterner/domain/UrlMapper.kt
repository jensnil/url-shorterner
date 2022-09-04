package com.urlshorterner.domain

import com.urlshorterner.configuration.UrlShorternerProperties.Companion.MAX_SHORT_URL_LENGTH
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotEmpty

@Entity
@Table(name ="url_mapper")
data class UrlMapper(
    @Id
    @Column(name = "short_url", length = MAX_SHORT_URL_LENGTH, unique = true, nullable = false, updatable = false)
    @NotEmpty
    val shortUrl: String,

    @Column(name = "long_url", length = MAX_SHORT_URL_LENGTH, unique = true, nullable = false, updatable = false)
    val longUrl: String,

    /**
     * Not used in this application, but potentially used for eviction due to age
     */
    @Column(name = "created", updatable = false, nullable = false)
    val created: LocalDateTime = LocalDateTime.now()
)
