package com.tedd.todo_project.domain.repository

import com.tedd.todo_project.domain.model.Todo
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TodoRepository {

    fun getTodos(): Flow<List<Todo>>

    suspend fun getTodoById(id: Long): Todo?

    suspend fun insertTodo(todo: Todo)

    suspend fun updateTodoCompletion(id: Long, isCompleted: Boolean, completedTime: LocalDateTime?)

    suspend fun updateTodoWork(id: Long, work: String, updatedTime: LocalDateTime?, position: Int)

    suspend fun deleteTodo(todo: Todo)

    suspend fun deleteTodos(todos: List<Todo>)

    suspend fun updateTodos(todos: List<Todo>)
}
