package com.urlshorterner.utils

fun getUrl(port: Int) = "http://localhost:${port}/url-shorterner/"

fun String.extractShortUrl() = this.substring(this.lastIndexOf("/") + 1)