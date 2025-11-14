package com.example.todoapi.presentation.dto

import com.example.todoapi.domain.model.Todo
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class TodoResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val completed: Boolean,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val createdAt: LocalDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(todo: Todo): TodoResponse {
            return TodoResponse(
                id = todo.id!!.value,
                title = todo.title,
                description = todo.description,
                completed = todo.completed,
                createdAt = todo.createdAt!!,
                updatedAt = todo.updatedAt!!
            )
        }
    }
}

data class CreateTodoRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 200, message = "Title must be 200 characters or less")
    val title: String,

    @field:Size(max = 1000, message = "Description must be 1000 characters or less")
    val description: String? = null
)

data class UpdateTodoRequest(
    @field:NotBlank(message = "Title is required")
    @field:Size(max = 200, message = "Title must be 200 characters or less")
    val title: String,

    @field:Size(max = 1000, message = "Description must be 1000 characters or less")
    val description: String? = null,

    val completed: Boolean = false
)

data class ErrorResponse(
    val message: String,
    val errors: List<String>? = null
)
