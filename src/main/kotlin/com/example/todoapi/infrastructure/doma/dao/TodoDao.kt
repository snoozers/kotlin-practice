package com.example.todoapi.infrastructure.doma.dao

import com.example.todoapi.infrastructure.doma.entity.TodoEntity
import org.seasar.doma.Dao
import org.seasar.doma.Delete
import org.seasar.doma.Insert
import org.seasar.doma.Select
import org.seasar.doma.Update
import org.seasar.doma.boot.ConfigAutowireable
import org.seasar.doma.jdbc.Result

@Dao
@ConfigAutowireable
interface TodoDao {

    @Select
    fun selectAll(): List<TodoEntity>

    @Select
    fun selectById(id: Long): TodoEntity?

    @Insert
    fun insert(entity: TodoEntity): Result<TodoEntity>

    @Update
    fun update(entity: TodoEntity): Result<TodoEntity>

    @Delete
    fun delete(entity: TodoEntity): Int
}
