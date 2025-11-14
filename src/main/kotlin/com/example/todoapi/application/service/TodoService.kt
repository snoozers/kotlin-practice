package com.example.todoapi.application.service

import com.example.todoapi.domain.model.Todo
import com.example.todoapi.domain.model.TodoId
import com.example.todoapi.domain.repository.TodoRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
@Transactional
class TodoService(
    private val todoRepository: TodoRepository
) {

    fun getAllTodos(): List<Todo> {
        logger.debug { "Getting all todos" }
        return todoRepository.findAll()
    }

    fun getTodoById(id: TodoId): Todo {
        logger.debug { "Getting todo by id: $id" }
        return todoRepository.findById(id)
            ?: throw TodoNotFoundException("Todo not found with id: ${id.value}")
    }

    fun createTodo(title: String, description: String?): Todo {
        logger.debug { "Creating new todo: title=$title" }
        val todo = Todo(
            id = null,
            title = title,
            description = description,
            completed = false,
            createdAt = null,
            updatedAt = null
        )
        return todoRepository.save(todo)
    }

    fun updateTodo(id: TodoId, title: String, description: String?, completed: Boolean): Todo {
        logger.debug { "Updating todo: id=$id" }
        val existingTodo = getTodoById(id)
        val updatedTodo = existingTodo.copy(
            title = title,
            description = description,
            completed = completed
        )
        return todoRepository.update(updatedTodo)
    }

    fun completeTodo(id: TodoId): Todo {
        logger.debug { "Completing todo: id=$id" }
        val todo = getTodoById(id)
        val completedTodo = todo.complete()
        return todoRepository.update(completedTodo)
    }

    fun incompleteTodo(id: TodoId): Todo {
        logger.debug { "Incompleting todo: id=$id" }
        val todo = getTodoById(id)
        val incompletedTodo = todo.incomplete()
        return todoRepository.update(incompletedTodo)
    }

    fun deleteTodo(id: TodoId) {
        logger.debug { "Deleting todo: id=$id" }
        val deleted = todoRepository.deleteById(id)
        if (!deleted) {
            throw TodoNotFoundException("Todo not found with id: ${id.value}")
        }
    }
}

class TodoNotFoundException(message: String) : RuntimeException(message)
