package com.urlshorterner.utils

import java.util.Date

fun Date.toMillis2() = this.toInstant().toEpochMilli()