package com.urlshorterner.errorhandling

import com.urlshorterner.utils.toMillis
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime
import javax.validation.ConstraintViolationException


@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler
    fun exception(exception: UrlMapperException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                LocalDateTime.now().toMillis(), exception.message ?: "Some error"),
            HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun exception(exception: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(
                LocalDateTime.now().toMillis(),
                exception.constraintViolations.firstOrNull()?.message ?: "Some error"),
            HttpStatus.BAD_REQUEST
        )
    }
}