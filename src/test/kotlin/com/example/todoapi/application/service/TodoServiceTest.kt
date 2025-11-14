package com.example.todoapi.application.service

import com.example.todoapi.TestcontainersConfig
import com.example.todoapi.domain.model.TodoId
import com.example.todoapi.domain.repository.TodoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfig::class)
@Transactional
class TodoServiceTest {

    @Autowired
    private lateinit var todoService: TodoService

    @Autowired
    private lateinit var todoRepository: TodoRepository

    @Test
    fun `should create todo`() {
        // When
        val todo = todoService.createTodo("Test Todo", "Test Description")

        // Then
        assertThat(todo.id).isNotNull
        assertThat(todo.title).isEqualTo("Test Todo")
        assertThat(todo.description).isEqualTo("Test Description")
        assertThat(todo.completed).isFalse
    }

    @Test
    fun `should get all todos`() {
        // Given
        todoService.createTodo("Todo 1", null)
        todoService.createTodo("Todo 2", null)

        // When
        val todos = todoService.getAllTodos()

        // Then
        assertThat(todos).hasSizeGreaterThanOrEqualTo(2)
    }

    @Test
    fun `should get todo by id`() {
        // Given
        val created = todoService.createTodo("Test Todo", null)

        // When
        val found = todoService.getTodoById(created.id!!)

        // Then
        assertThat(found.id).isEqualTo(created.id)
        assertThat(found.title).isEqualTo("Test Todo")
    }

    @Test
    fun `should throw exception when todo not found`() {
        // When & Then
        assertThrows<TodoNotFoundException> {
            todoService.getTodoById(TodoId(999999))
        }
    }

    @Test
    fun `should update todo`() {
        // Given
        val created = todoService.createTodo("Original Title", "Original Description")

        // When
        val updated = todoService.updateTodo(
            created.id!!,
            "Updated Title",
            "Updated Description",
            true
        )

        // Then
        assertThat(updated.id).isEqualTo(created.id)
        assertThat(updated.title).isEqualTo("Updated Title")
        assertThat(updated.description).isEqualTo("Updated Description")
        assertThat(updated.completed).isTrue
    }

    @Test
    fun `should complete todo`() {
        // Given
        val created = todoService.createTodo("Test Todo", null)
        assertThat(created.completed).isFalse

        // When
        val completed = todoService.completeTodo(created.id!!)

        // Then
        assertThat(completed.completed).isTrue
    }

    @Test
    fun `should incomplete todo`() {
        // Given
        val created = todoService.createTodo("Test Todo", null)
        val completed = todoService.completeTodo(created.id!!)
        assertThat(completed.completed).isTrue

        // When
        val incompleted = todoService.incompleteTodo(created.id!!)

        // Then
        assertThat(incompleted.completed).isFalse
    }

    @Test
    fun `should delete todo`() {
        // Given
        val created = todoService.createTodo("Test Todo", null)

        // When
        todoService.deleteTodo(created.id!!)

        // Then
        assertThrows<TodoNotFoundException> {
            todoService.getTodoById(created.id!!)
        }
    }

    @Test
    fun `should throw exception when deleting non-existent todo`() {
        // When & Then
        assertThrows<TodoNotFoundException> {
            todoService.deleteTodo(TodoId(999999))
        }
    }

    @Test
    fun `should not create todo with blank title`() {
        // When & Then
        assertThrows<IllegalArgumentException> {
            todoService.createTodo("", null)
        }
    }

    @Test
    fun `should not create todo with title exceeding max length`() {
        // Given
        val longTitle = "a".repeat(201)

        // When & Then
        assertThrows<IllegalArgumentException> {
            todoService.createTodo(longTitle, null)
        }
    }
}
