package dev.ufadevconf.infrastructure.api

import dev.ufadevconf.application.PickupPointNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(PickupPointNotFoundException::class)
    fun handlePickupPointNotFound(exception: PickupPointNotFoundException): ResponseEntity<Map<String, String>> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(mapOf("error" to exception.message.orEmpty()))
}
