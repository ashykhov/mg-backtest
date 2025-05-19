package com.shykhov.backtest.api.common.cadvice

import com.shykhov.common.backtest.BadBacktestRequest
import com.shykhov.common.backtest.timeframe.TimeFrame
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.time.Clock

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

    @ExceptionHandler(BadBacktestRequest::class)
    fun handleBadBacktestRequest(ex: BadBacktestRequest, request: WebRequest): ResponseEntity<ErrorResponse> {
        val requested = TimeFrame.fromValue(ex.req.second.name)
        val allowed = ex.allowed.map { TimeFrame.fromValue(it.second.name) }
        val errorResponse = ErrorResponse(
            timestamp = clock.instant(),
            status = BAD_REQUEST.value(),
            error = BAD_REQUEST.reasonPhrase,
            message = "TimeFrame $requested is disabled. Allowed: $allowed",
            path = request.getDescription(false).substring(4),
        )

        return ResponseEntity(errorResponse, BAD_REQUEST)
    }
}
