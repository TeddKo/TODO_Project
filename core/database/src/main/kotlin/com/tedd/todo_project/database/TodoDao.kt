package com.tedd.todo_project.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos ORDER BY addedTime DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>

    @Insert
    suspend fun insertTodo(todo: TodoEntity)

    @Update
    suspend fun updateTodo(todo: TodoEntity)

    @Delete
    suspend fun deleteTodo(todo: TodoEntity)

    @Query("SELECT * FROM todos WHERE id = :todoId")
    suspend fun getTodoById(todoId: Long): TodoEntity?

    @Query("UPDATE todos SET isCompleted = :isCompleted, completedTime = :completedTime WHERE id = :id")
    suspend fun updateTodoCompletion(id: Long, isCompleted: Boolean, completedTime: LocalDateTime?)
}