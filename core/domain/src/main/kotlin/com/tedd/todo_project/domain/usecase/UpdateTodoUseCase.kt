package com.tedd.todo_project.domain.usecase

import com.tedd.todo_project.domain.repository.TodoRepository
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class UpdateTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(id: Long, isCompleted: Boolean, completedTime: LocalDateTime?) {
        todoRepository.updateTodo(id, isCompleted, completedTime)
    }
}