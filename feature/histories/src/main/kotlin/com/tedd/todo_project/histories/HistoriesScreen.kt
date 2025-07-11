package com.tedd.todo_project.histories

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tedd.todo_project.core.designsystem.R
import com.tedd.todo_project.histories.components.HistoriesColumn
import com.tedd.todo_project.histories.viewmodel.HistoriesScreenIntent
import com.tedd.todo_project.histories.viewmodel.HistoriesScreenState
import com.tedd.todo_project.ui.components.HistoriesTopAppBar

@Composable
fun HistoriesScreen(
    uiState: HistoriesScreenState,
    onIntent: (HistoriesScreenIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        HistoriesTopAppBar(
            title = stringResource(R.string.history),
            onNavigationClick = { onIntent(HistoriesScreenIntent.OnNavigateBack) }
        )

        if (uiState.completedTodos.isEmpty()) {
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
                items(uiState.completedTodos, key = { it.id }) { todo ->
                    HistoriesColumn(
                        todoText = todo.work,
                        registrationDate = todo.addedTime,
                        completionDate = todo.completedTime
                    )
                }
            }
        }
    }
}
