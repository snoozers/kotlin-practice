package com.example.todoapi.infrastructure.doma.entity

import com.example.todoapi.domain.model.Todo
import com.example.todoapi.domain.model.TodoId
import org.seasar.doma.Entity
import org.seasar.doma.GeneratedValue
import org.seasar.doma.GenerationType
import org.seasar.doma.Id
import org.seasar.doma.Table
import java.time.LocalDateTime

@Entity(immutable = true)
@Table(name = "todos")
data class TodoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val completed: Boolean = false,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    fun toDomain(): Todo {
        return Todo(
            id = this.id?.let { TodoId(it) },
            title = this.title,
            description = this.description,
            completed = this.completed,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    companion object {
        fun fromDomain(todo: Todo): TodoEntity {
            return TodoEntity(
                id = todo.id?.value,
                title = todo.title,
                description = todo.description,
                completed = todo.completed,
                createdAt = todo.createdAt,
                updatedAt = todo.updatedAt
            )
        }
    }
}
