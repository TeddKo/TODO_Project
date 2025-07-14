package com.tedd.todo_project.histories.viewmodel

import androidx.compose.runtime.Immutable
import com.tedd.todo_project.domain.model.Todo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class HistoriesScreenState(
    val histories: ImmutableList<Todo> = persistentListOf(),
    val isSelectionMode: Boolean = false,
    val selectedHistoriesIds: Set<Long> = emptySet(),
    val isLoading: Boolean = false,
    val swipingHistoryId: Long? = null
)
