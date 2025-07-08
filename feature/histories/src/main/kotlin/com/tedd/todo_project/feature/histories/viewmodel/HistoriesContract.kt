package com.tedd.todo_project.feature.histories.viewmodel

sealed interface HistoriesScreenIntent {
    object OnNavigateBack : HistoriesScreenIntent
}
