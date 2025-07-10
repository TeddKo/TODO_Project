package com.tedd.todo_project.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tedd.todo_project.ui.components.MainTodoTopAppBar
import com.tedd.todo_project.ui.extensions.addFocusCleaner
import com.tedd.todo_project.main.components.SwipeableTodoItem
import com.tedd.todo_project.main.components.TodoEditText
import com.tedd.todo_project.main.viewmodel.MainScreenEvent
import com.tedd.todo_project.main.viewmodel.MainScreenState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: () -> StateFlow<MainScreenState>,
    onEvent: (MainScreenEvent) -> Unit
) {
    val uiState by uiState().collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
    ) {
        MainTodoTopAppBar(
            title = "DEEP.FINE TODO",
            onNavigationClick = {
                onEvent(MainScreenEvent.OnNavigate)
            }
        )
        if (uiState.todos.isEmpty()) {
            Box(Modifier
                .weight(1f)
                .fillMaxSize()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    text = "TODO List가 없습니다."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.todos, key = { it.id }) {
                    SwipeableTodoItem(
                        todo = it,
                        onToggleComplete = { onEvent(MainScreenEvent.ToggleTodoComplete(it)) },
                        onDelete = { onEvent(MainScreenEvent.DeleteTodo(it)) }
                    )
                }
            }
        }

        TodoEditText(
            modifier = Modifier.fillMaxWidth(),
            text = uiState.todoInput,
            onTextChange = { onEvent(MainScreenEvent.UpdateTodoInput(it)) },
            onAddTodo = { onEvent(MainScreenEvent.AddTodo) }
        )
    }
}
