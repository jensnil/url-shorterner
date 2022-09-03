package com.urlshorterner.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UrlMapperRepository: JpaRepository<UrlMapper, Long> {
	fun findByLongUrl(longUrl:String):UrlMapper?
	fun findByShortUrl(shortUrl:String):UrlMapper?
}
