package com.example.todoapi.domain.repository

import com.example.todoapi.domain.model.Todo
import com.example.todoapi.domain.model.TodoId

interface TodoRepository {
    fun findAll(): List<Todo>
    fun findById(id: TodoId): Todo?
    fun save(todo: Todo): Todo
    fun update(todo: Todo): Todo
    fun deleteById(id: TodoId): Boolean
}
