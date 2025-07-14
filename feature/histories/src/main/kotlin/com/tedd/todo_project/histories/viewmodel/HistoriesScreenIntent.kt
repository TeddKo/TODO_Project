package com.tedd.todo_project.histories.viewmodel

import androidx.compose.runtime.Stable
import com.tedd.todo_project.domain.model.Todo

@Stable
sealed interface HistoriesScreenIntent {
    data class OnDeleteHistory(val todo: Todo) : HistoriesScreenIntent
    data object OnClearSelection : HistoriesScreenIntent
    data object OnSelectAllHistories : HistoriesScreenIntent
    data object OnDeleteSelectedHistories : HistoriesScreenIntent
    data class OnSelectHistory(val historyId: Long) : HistoriesScreenIntent
    data object OnNavigateBack : HistoriesScreenIntent
    data class OnSwipeStateChange(val historyId: Long, val isSwiping: Boolean) : HistoriesScreenIntent
}
