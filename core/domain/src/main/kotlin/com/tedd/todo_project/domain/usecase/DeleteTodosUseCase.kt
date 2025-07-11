package com.tedd.todo_project.domain.usecase

import com.tedd.todo_project.domain.model.Todo
import com.tedd.todo_project.domain.repository.TodoRepository
import javax.inject.Inject

class DeleteTodosUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(todos: List<Todo>) {
        todoRepository.deleteTodos(todos = todos)
    }
}