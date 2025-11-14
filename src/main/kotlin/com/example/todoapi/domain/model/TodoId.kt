package com.example.todoapi.domain.model

@JvmInline
value class TodoId(val value: Long) {
    init {
        require(value > 0) { "TodoId must be positive" }
    }
}
