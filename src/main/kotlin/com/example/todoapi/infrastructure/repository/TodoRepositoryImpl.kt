package com.example.todoapi.infrastructure.repository

import com.example.todoapi.domain.model.Todo
import com.example.todoapi.domain.model.TodoId
import com.example.todoapi.domain.repository.TodoRepository
import com.example.todoapi.infrastructure.doma.dao.TodoDao
import com.example.todoapi.infrastructure.doma.entity.TodoEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class TodoRepositoryImpl(
    private val todoDao: TodoDao
) : TodoRepository {

    override fun findAll(): List<Todo> {
        return todoDao.selectAll().map { it.toDomain() }
    }

    override fun findById(id: TodoId): Todo? {
        return todoDao.selectById(id.value)?.toDomain()
    }

    override fun save(todo: Todo): Todo {
        val entity = TodoEntity.fromDomain(todo)
        val result = todoDao.insert(entity)
        return result.entity.toDomain()
    }

    override fun update(todo: Todo): Todo {
        val entity = TodoEntity.fromDomain(todo)
        val result = todoDao.update(entity)
        return result.entity.toDomain()
    }

    override fun deleteById(id: TodoId): Boolean {
        val entity = todoDao.selectById(id.value) ?: return false
        return todoDao.delete(entity) > 0
    }
}
