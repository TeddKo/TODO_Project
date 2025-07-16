package com.tedd.todo_project.domain.usecase

import com.tedd.todo_project.domain.repository.TodoRepository
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject

class UpdateTodoWorkUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    suspend operator fun invoke(
        id: Long,
        work: String,
        updatedTime: LocalDateTime? = null
    ) {
        todoRepository.updateTodoWork(
            id = id,
            work = work,
            updatedTime = updatedTime
        )
    }
}