package com.urlshorterner.domain

import com.urlshorterner.configuration.UrlShorternerProperties.Companion.MAX_SHORT_URL_LENGTH
import com.urlshorterner.errorhandling.Url
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.GenerationType.AUTO
import javax.persistence.GenerationType.IDENTITY
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table
import javax.validation.constraints.Max
import javax.validation.constraints.NotEmpty

@Entity
@Table(name ="url_mapper")
data class UrlMapper(
    @Id
    @Column(name = "short_url", length = MAX_SHORT_URL_LENGTH, unique = true, nullable = false, updatable = false)
    @NotEmpty
    val shortUrl: String,

    @Column(name = "long_url", length = MAX_SHORT_URL_LENGTH, unique = true, nullable = false, updatable = false)
    @Url
    val longUrl: String,

    /**
     * Not used in this application, but potentially used for eviction due to age
     */
    @Column(name = "created", updatable = false, nullable = false)
    val created: LocalDateTime = LocalDateTime.now(),
    )
