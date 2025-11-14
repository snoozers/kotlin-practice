package com.example.todoapi.presentation.exception

import com.example.todoapi.application.service.TodoNotFoundException
import com.example.todoapi.presentation.dto.ErrorResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(TodoNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleTodoNotFoundException(e: TodoNotFoundException): ErrorResponse {
        logger.warn { "Todo not found: ${e.message}" }
        return ErrorResponse(message = e.message ?: "Todo not found")
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ErrorResponse {
        logger.warn { "Invalid argument: ${e.message}" }
        return ErrorResponse(message = e.message ?: "Invalid argument")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidationException(e: MethodArgumentNotValidException): ErrorResponse {
        val errors = e.bindingResult.allErrors.map { error ->
            val fieldName = (error as? FieldError)?.field ?: "unknown"
            val message = error.defaultMessage ?: "validation error"
            "$fieldName: $message"
        }
        logger.warn { "Validation error: $errors" }
        return ErrorResponse(
            message = "Validation failed",
            errors = errors
        )
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): ErrorResponse {
        logger.error(e) { "Unexpected error: ${e.message}" }
        return ErrorResponse(message = "Internal server error")
    }
}
