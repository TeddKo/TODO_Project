package com.tedd.todo_project.domain.repository

import com.tedd.todo_project.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    fun getTodos(): Flow<List<Todo>>

    suspend fun getTodoById(id: Long): Todo?

    suspend fun insertTodo(todo: Todo)

    suspend fun updateTodo(id: Long, isCompleted: Boolean, completedTime: kotlinx.datetime.LocalDateTime?)

    suspend fun deleteTodo(todo: Todo)
}
