package com.urlshorterner.infrastructure

import com.urlshorterner.application.UrlShorternerService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
@RequestMapping
class UrlShorternerController(val urlShorternerService: UrlShorternerService) {

	@PostMapping(path = ["/"])
	fun create(@RequestBody longUrl: String): String {
		return urlShorternerService.create(longUrl)
	}

	@GetMapping(path = ["/{shortUrl}"])
	fun lookup(@PathVariable(value = "shortUrl", required = true) shortUrl: String): ModelAndView {
		return ModelAndView("redirect:${urlShorternerService.lookup(shortUrl)}");
	}
}