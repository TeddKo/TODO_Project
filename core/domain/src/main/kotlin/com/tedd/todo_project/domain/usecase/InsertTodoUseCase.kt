package com.tedd.todo_project.domain.usecase

import com.tedd.todo_project.domain.model.Todo
import com.tedd.todo_project.domain.repository.TodoRepository
import javax.inject.Inject

class InsertTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(todo: Todo) = repository.insertTodo(todo)
}
