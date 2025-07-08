package com.tedd.todo_project.feature.histories.viewmodel

import androidx.compose.runtime.Immutable
import com.tedd.todo_project.domain.model.Todo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class HistoriesScreenState(
    val completedTodos: ImmutableList<Todo> = persistentListOf()
)
