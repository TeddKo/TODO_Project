package com.tedd.todo_project.domain.usecase

import com.tedd.todo_project.domain.model.Todo
import com.tedd.todo_project.domain.repository.TodoRepository
import javax.inject.Inject

class UpdateTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(id: Long, isCompleted: Boolean, completedTime: kotlinx.datetime.LocalDateTime?) {
        todoRepository.updateTodo(id, isCompleted, completedTime)
    }
}