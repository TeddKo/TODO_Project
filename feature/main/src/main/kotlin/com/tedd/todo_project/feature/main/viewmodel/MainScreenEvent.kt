package com.tedd.todo_project.feature.main.viewmodel

import androidx.compose.runtime.Stable
import com.tedd.todo_project.domain.model.Todo

@Stable
sealed interface MainScreenEvent {
    data class UpdateTodoInput(val newInput: String) : MainScreenEvent
    data object AddTodo : MainScreenEvent
    data class ToggleTodoComplete(val todo: Todo) : MainScreenEvent
    data class DeleteTodo(val todo: Todo) : MainScreenEvent
    data object OnNavigate: MainScreenEvent
}
