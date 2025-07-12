package com.tedd.todo_project.main.viewmodel

import androidx.compose.runtime.Stable
import com.tedd.todo_project.domain.model.Todo

@Stable
sealed interface MainScreenEvent {
    data class UpdateTodoInput(val newInput: String) : MainScreenEvent
    data object AddTodo : MainScreenEvent
    data class ToggleTodoComplete(val todo: Todo) : MainScreenEvent
    data class DeleteTodo(val todo: Todo) : MainScreenEvent
    data object OnNavigate: MainScreenEvent
    data class OnTodoSelectionClick(val todoId: Long) : MainScreenEvent
    data object ClearSelection : MainScreenEvent
    data object DeleteSelectedTodos : MainScreenEvent
    data class OnMoveTodo(val fromIndex: Int, val toIndex: Int) : MainScreenEvent
    data object OnUpdateTodos: MainScreenEvent
    data object OnSelectAllTodos: MainScreenEvent
    data class OnSwipeStateChange(val todoId: Long, val isSwiping: Boolean) : MainScreenEvent
}
