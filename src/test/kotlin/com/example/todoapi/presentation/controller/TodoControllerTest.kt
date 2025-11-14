package com.example.todoapi.presentation.controller

import com.example.todoapi.TestcontainersConfig
import com.example.todoapi.application.service.TodoService
import com.example.todoapi.presentation.dto.CreateTodoRequest
import com.example.todoapi.presentation.dto.UpdateTodoRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfig::class)
@Transactional
class TodoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var todoService: TodoService

    private var testTodoId: Long = 0

    @BeforeEach
    fun setup() {
        // Create a test todo
        val todo = todoService.createTodo("Test Todo", "Test Description")
        testTodoId = todo.id!!.value
    }

    @Test
    fun `should get all todos`() {
        mockMvc.perform(get("/api/todos"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
    }

    @Test
    fun `should get todo by id`() {
        mockMvc.perform(get("/api/todos/$testTodoId"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(testTodoId))
            .andExpect(jsonPath("$.title").value("Test Todo"))
            .andExpect(jsonPath("$.description").value("Test Description"))
            .andExpect(jsonPath("$.completed").value(false))
    }

    @Test
    fun `should return 404 when todo not found`() {
        mockMvc.perform(get("/api/todos/999999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should create todo`() {
        val request = CreateTodoRequest(
            title = "New Todo",
            description = "New Description"
        )

        mockMvc.perform(
            post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").isNumber)
            .andExpect(jsonPath("$.title").value("New Todo"))
            .andExpect(jsonPath("$.description").value("New Description"))
            .andExpect(jsonPath("$.completed").value(false))
    }

    @Test
    fun `should return 400 when creating todo with blank title`() {
        val request = CreateTodoRequest(
            title = "",
            description = "Description"
        )

        mockMvc.perform(
            post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should update todo`() {
        val request = UpdateTodoRequest(
            title = "Updated Todo",
            description = "Updated Description",
            completed = true
        )

        mockMvc.perform(
            put("/api/todos/$testTodoId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(testTodoId))
            .andExpect(jsonPath("$.title").value("Updated Todo"))
            .andExpect(jsonPath("$.description").value("Updated Description"))
            .andExpect(jsonPath("$.completed").value(true))
    }

    @Test
    fun `should complete todo`() {
        mockMvc.perform(patch("/api/todos/$testTodoId/complete"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(testTodoId))
            .andExpect(jsonPath("$.completed").value(true))
    }

    @Test
    fun `should incomplete todo`() {
        // First complete the todo
        todoService.completeTodo(todoService.getTodoById(com.example.todoapi.domain.model.TodoId(testTodoId)).id!!)

        mockMvc.perform(patch("/api/todos/$testTodoId/incomplete"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(testTodoId))
            .andExpect(jsonPath("$.completed").value(false))
    }

    @Test
    fun `should delete todo`() {
        mockMvc.perform(delete("/api/todos/$testTodoId"))
            .andExpect(status().isNoContent)

        // Verify deletion
        mockMvc.perform(get("/api/todos/$testTodoId"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should return 404 when deleting non-existent todo`() {
        mockMvc.perform(delete("/api/todos/999999"))
            .andExpect(status().isNotFound)
    }
}
