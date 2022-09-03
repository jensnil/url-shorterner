package com.urlshorterner.errorhandling

data class ErrorResponse(
    val timestamp: Long, //In millis
    val error: String
)