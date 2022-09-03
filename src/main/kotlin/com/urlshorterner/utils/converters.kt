package com.urlshorterner.utils

import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toMillis() = this.toInstant(ZoneOffset.UTC).toEpochMilli()