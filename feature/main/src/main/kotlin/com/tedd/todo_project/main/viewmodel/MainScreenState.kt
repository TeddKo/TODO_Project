package com.tedd.todo_project.main.viewmodel

import androidx.compose.runtime.Immutable
import com.tedd.todo_project.domain.model.Todo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class MainScreenState(
    val todos: ImmutableList<Todo> = persistentListOf(),
    val todoInput: String = "",
    val isSelectionMode: Boolean = false,
    val selectedTodoIds: Set<Long> = emptySet()
)
