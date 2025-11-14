package com.example.todoapi.presentation.controller

import com.example.todoapi.application.service.TodoService
import com.example.todoapi.domain.model.TodoId
import com.example.todoapi.presentation.dto.CreateTodoRequest
import com.example.todoapi.presentation.dto.TodoResponse
import com.example.todoapi.presentation.dto.UpdateTodoRequest
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/todos")
class TodoController(
    private val todoService: TodoService
) {

    @GetMapping
    fun getAllTodos(): List<TodoResponse> {
        logger.debug { "GET /api/todos" }
        return todoService.getAllTodos().map { TodoResponse.from(it) }
    }

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: Long): TodoResponse {
        logger.debug { "GET /api/todos/$id" }
        val todo = todoService.getTodoById(TodoId(id))
        return TodoResponse.from(todo)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTodo(@Valid @RequestBody request: CreateTodoRequest): TodoResponse {
        logger.debug { "POST /api/todos: $request" }
        val todo = todoService.createTodo(request.title, request.description)
        return TodoResponse.from(todo)
    }

    @PutMapping("/{id}")
    fun updateTodo(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateTodoRequest
    ): TodoResponse {
        logger.debug { "PUT /api/todos/$id: $request" }
        val todo = todoService.updateTodo(
            TodoId(id),
            request.title,
            request.description,
            request.completed
        )
        return TodoResponse.from(todo)
    }

    @PatchMapping("/{id}/complete")
    fun completeTodo(@PathVariable id: Long): TodoResponse {
        logger.debug { "PATCH /api/todos/$id/complete" }
        val todo = todoService.completeTodo(TodoId(id))
        return TodoResponse.from(todo)
    }

    @PatchMapping("/{id}/incomplete")
    fun incompleteTodo(@PathVariable id: Long): TodoResponse {
        logger.debug { "PATCH /api/todos/$id/incomplete" }
        val todo = todoService.incompleteTodo(TodoId(id))
        return TodoResponse.from(todo)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTodo(@PathVariable id: Long) {
        logger.debug { "DELETE /api/todos/$id" }
        todoService.deleteTodo(TodoId(id))
    }
}
