package com.tedd.todo_project.main.viewmodel

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.input.TextFieldValue
import com.tedd.todo_project.domain.model.Todo

@Stable
sealed interface MainScreenIntent {
    data class OnUpdateTodoInput(val newInput: TextFieldValue) : MainScreenIntent
    data object OnAddTodo : MainScreenIntent
    data class OnToggleTodoComplete(val todo: Todo) : MainScreenIntent
    data class OnUpdateTodoWork(val newInput: String) : MainScreenIntent
    data class OnDeleteTodo(val todo: Todo) : MainScreenIntent
    data object OnNavigate : MainScreenIntent
    data class OnSelectTodo(val todoId: Long) : MainScreenIntent
    data object OnClearSelection : MainScreenIntent
    data object OnDeleteSelectedTodos : MainScreenIntent
    data class OnMoveTodo(val fromIndex: Int, val toIndex: Int) : MainScreenIntent
    data object OnEditTodo : MainScreenIntent
    data object OnEditCancel : MainScreenIntent
    data object OnUpdateTodoIndex : MainScreenIntent
    data object OnSelectAllTodos : MainScreenIntent
    data class OnSwipeStateChange(val todoId: Long, val isSwiping: Boolean) : MainScreenIntent
}
