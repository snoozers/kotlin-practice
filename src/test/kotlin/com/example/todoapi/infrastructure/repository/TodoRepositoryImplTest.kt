package com.example.todoapi.infrastructure.repository

import com.example.todoapi.TestcontainersConfig
import com.example.todoapi.domain.model.Todo
import com.example.todoapi.domain.model.TodoId
import com.example.todoapi.domain.repository.TodoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Import(TestcontainersConfig::class)
@Transactional
class TodoRepositoryImplTest {

    @Autowired
    private lateinit var todoRepository: TodoRepository

    @BeforeEach
    fun setup() {
        // Clean up before each test
    }

    @Test
    fun `should save and find todo`() {
        // Given
        val todo = Todo(
            id = null,
            title = "Test Todo",
            description = "Test Description",
            completed = false,
            createdAt = null,
            updatedAt = null
        )

        // When
        val saved = todoRepository.save(todo)

        // Then
        assertThat(saved.id).isNotNull
        assertThat(saved.title).isEqualTo("Test Todo")
        assertThat(saved.description).isEqualTo("Test Description")
        assertThat(saved.completed).isFalse
        assertThat(saved.createdAt).isNotNull
        assertThat(saved.updatedAt).isNotNull
    }

    @Test
    fun `should find todo by id`() {
        // Given
        val todo = Todo(
            id = null,
            title = "Test Todo",
            description = "Test Description",
            completed = false,
            createdAt = null,
            updatedAt = null
        )
        val saved = todoRepository.save(todo)

        // When
        val found = todoRepository.findById(saved.id!!)

        // Then
        assertThat(found).isNotNull
        assertThat(found?.id).isEqualTo(saved.id)
        assertThat(found?.title).isEqualTo("Test Todo")
    }

    @Test
    fun `should return null when todo not found`() {
        // When
        val found = todoRepository.findById(TodoId(999999))

        // Then
        assertThat(found).isNull()
    }

    @Test
    fun `should find all todos`() {
        // Given
        val todo1 = Todo(null, "Todo 1", null, false, null, null)
        val todo2 = Todo(null, "Todo 2", null, false, null, null)
        todoRepository.save(todo1)
        todoRepository.save(todo2)

        // When
        val todos = todoRepository.findAll()

        // Then
        assertThat(todos).hasSizeGreaterThanOrEqualTo(2)
        assertThat(todos.map { it.title }).contains("Todo 1", "Todo 2")
    }

    @Test
    fun `should update todo`() {
        // Given
        val todo = Todo(null, "Original Title", "Original Description", false, null, null)
        val saved = todoRepository.save(todo)

        // When
        val updated = saved.copy(
            title = "Updated Title",
            description = "Updated Description",
            completed = true
        )
        val result = todoRepository.update(updated)

        // Then
        assertThat(result.id).isEqualTo(saved.id)
        assertThat(result.title).isEqualTo("Updated Title")
        assertThat(result.description).isEqualTo("Updated Description")
        assertThat(result.completed).isTrue
    }

    @Test
    fun `should delete todo by id`() {
        // Given
        val todo = Todo(null, "Test Todo", null, false, null, null)
        val saved = todoRepository.save(todo)

        // When
        val deleted = todoRepository.deleteById(saved.id!!)

        // Then
        assertThat(deleted).isTrue
        assertThat(todoRepository.findById(saved.id!!)).isNull()
    }

    @Test
    fun `should return false when deleting non-existent todo`() {
        // When
        val deleted = todoRepository.deleteById(TodoId(999999))

        // Then
        assertThat(deleted).isFalse
    }
}
