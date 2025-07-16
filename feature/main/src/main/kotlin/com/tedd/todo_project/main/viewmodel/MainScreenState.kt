package com.tedd.todo_project.main.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.input.TextFieldValue
import com.tedd.todo_project.domain.model.Todo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MainScreenState(
    val todos: ImmutableList<Todo> = persistentListOf(),
    val todoInput: TextFieldValue = TextFieldValue(""),
    val isSelectionMode: Boolean = false,
    val selectedTodoIds: Set<Long> = emptySet(),
    val isLoading: Boolean = false,
    val swipingTodoId: Long? = null,
    val isUpdatableWork: Boolean = false,
) {
    val isEditable: Boolean
        get() = isSelectionMode && selectedTodoIds.size == 1

    val isSelectionTobBarVisible: Boolean get() = isSelectionMode && !isUpdatableWork
}
