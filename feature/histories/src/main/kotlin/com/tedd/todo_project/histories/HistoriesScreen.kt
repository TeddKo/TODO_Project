package com.tedd.todo_project.histories

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tedd.todo_project.core.designsystem.R
import com.tedd.todo_project.histories.components.HistoriesColumn
import com.tedd.todo_project.histories.viewmodel.HistoriesScreenIntent
import com.tedd.todo_project.histories.viewmodel.HistoriesScreenState
import com.tedd.todo_project.ui.components.HistoriesTopAppBar
import com.tedd.todo_project.ui.components.LoadingDialog
import com.tedd.todo_project.ui.components.SelectionDeleteTopAppBar
import com.tedd.todo_project.ui.components.SwipeableTodoItem

@Composable
fun HistoriesScreen(
    uiState: HistoriesScreenState,
    onIntent: (HistoriesScreenIntent) -> Unit
) {

    if (uiState.isLoading) {
        LoadingDialog()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {

        if (uiState.isSelectionMode) {
            SelectionDeleteTopAppBar(
                title = stringResource(R.string.selected_item, uiState.selectedHistoriesIds.size),
                isAllSelected = uiState.histories.isNotEmpty() && uiState.selectedHistoriesIds.size == uiState.histories.size,
                onClearSelection = { onIntent(HistoriesScreenIntent.OnClearSelection) },
                onSelectAll = { onIntent(HistoriesScreenIntent.OnSelectAllHistories) },
                onDeleteSelected = { onIntent(HistoriesScreenIntent.OnDeleteSelectedHistories) }
            )
        } else {
            HistoriesTopAppBar(
                title = stringResource(R.string.history),
                onNavigationClick = { onIntent(HistoriesScreenIntent.OnNavigateBack) }
            )
        }

        if (uiState.histories.isEmpty() && !uiState.isLoading) {
            Box(Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    text = stringResource(R.string.empty_histories)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(uiState.histories, key = { it.id }) { todo ->
                    SwipeableTodoItem(
                        cornerRadius = 10.dp,
                        elevation = 0.dp,
                        onDelete = { onIntent(HistoriesScreenIntent.OnDeleteHistory(todo)) },
                        onClick = { onIntent(HistoriesScreenIntent.OnSelectHistory(historyId = todo.id)) },
                        clickEnabled = uiState.swipingHistoryId == null,
                        gestureEnabled = !uiState.isSelectionMode && (uiState.swipingHistoryId == null || uiState.swipingHistoryId == todo.id),
                        onDismissStateChanged = { newDismissState ->
                            val isSwiping = newDismissState != SwipeToDismissBoxValue.Settled
                            onIntent(
                                HistoriesScreenIntent.OnSwipeStateChange(
                                    historyId = todo.id,
                                    isSwiping = isSwiping
                                )
                            )
                        }
                    ) {

                        val backgroundColor by animateColorAsState(
                            targetValue = if (todo.id in uiState.selectedHistoriesIds) MaterialTheme.colorScheme.secondary
                            else MaterialTheme.colorScheme.surface,
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearEasing
                            )
                        )

                        HistoriesColumn(
                            todoText = todo.work,
                            backgroundColor = backgroundColor,
                            registrationDate = todo.addedTime,
                            completionDate = todo.completedTime
                        )
                    }
                }
            }
        }
    }
}
