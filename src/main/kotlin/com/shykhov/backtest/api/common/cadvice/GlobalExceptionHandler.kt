package com.shykhov.backtest.api.common.cadvice

import java.time.Clock
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler(
    private val clock: Clock,
) {
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        val status = when (ex) {
            is IllegalArgumentException -> BAD_REQUEST
            else -> INTERNAL_SERVER_ERROR
        }

        val errorResponse = ErrorResponse(
            timestamp = clock.instant(),
            status = status.value(),
            error = status.reasonPhrase,
            message = ex.message,
            // Убираем 'uri=' из начала
            path = request.getDescription(false).substring(4),
        )

        return ResponseEntity(errorResponse, status)
    }


}
