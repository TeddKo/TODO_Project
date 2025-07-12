package com.tedd.todo_project.histories.viewmodel

import androidx.compose.runtime.Stable

@Stable
sealed interface HistoriesScreenIntent {
    data object OnNavigateBack : HistoriesScreenIntent
}
