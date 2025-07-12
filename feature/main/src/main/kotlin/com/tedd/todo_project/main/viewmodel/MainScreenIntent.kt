package com.tedd.todo_project.main.viewmodel

import androidx.compose.runtime.Stable
import com.tedd.todo_project.domain.model.Todo

@Stable
sealed interface MainScreenIntent {
    data class UpdateTodoInput(val newInput: String) : MainScreenIntent
    data object AddTodo : MainScreenIntent
    data class ToggleTodoComplete(val todo: Todo) : MainScreenIntent
    data class DeleteTodo(val todo: Todo) : MainScreenIntent
    data object OnNavigate: MainScreenIntent
    data class OnTodoSelectionClick(val todoId: Long) : MainScreenIntent
    data object ClearSelection : MainScreenIntent
    data object DeleteSelectedTodos : MainScreenIntent
    data class OnMoveTodo(val fromIndex: Int, val toIndex: Int) : MainScreenIntent
    data object OnUpdateTodos: MainScreenIntent
    data object OnSelectAllTodos: MainScreenIntent
    data class OnSwipeStateChange(val todoId: Long, val isSwiping: Boolean) : MainScreenIntent
}
