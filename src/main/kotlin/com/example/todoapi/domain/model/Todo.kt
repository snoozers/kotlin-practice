package com.example.todoapi.domain.model

import java.time.LocalDateTime

data class Todo(
    val id: TodoId?,
    val title: String,
    val description: String?,
    val completed: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) {
    init {
        require(title.isNotBlank()) { "Title must not be blank" }
        require(title.length <= 200) { "Title must be 200 characters or less" }
        description?.let {
            require(it.length <= 1000) { "Description must be 1000 characters or less" }
        }
    }

    fun complete(): Todo = copy(completed = true)
    fun incomplete(): Todo = copy(completed = false)
}
