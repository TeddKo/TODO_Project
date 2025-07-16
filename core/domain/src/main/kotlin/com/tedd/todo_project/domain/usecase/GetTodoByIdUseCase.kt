package com.tedd.todo_project.domain.usecase

import com.tedd.todo_project.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoByIdUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(id: Long) = repository.getTodoById(id)
}