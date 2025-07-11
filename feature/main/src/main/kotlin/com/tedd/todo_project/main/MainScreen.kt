package com.tedd.todo_project.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tedd.todo_project.main.components.SwipeableTodoItem
import com.tedd.todo_project.main.components.TodoEditText
import com.tedd.todo_project.main.viewmodel.MainScreenEvent
import com.tedd.todo_project.main.viewmodel.MainScreenState
import com.tedd.todo_project.ui.components.MainTodoTopAppBar
import com.tedd.todo_project.ui.extensions.addFocusCleaner
import kotlinx.coroutines.flow.StateFlow
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    uiState: () -> StateFlow<MainScreenState>,
    onEvent: (MainScreenEvent) -> Unit
) {
    val uiState by uiState().collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    var swipingTodoId by remember { mutableStateOf<Long?>(null) }

    val lazyListState = rememberLazyListState()
    val state =
        rememberReorderableLazyListState(
            lazyListState = lazyListState,
            onMove = { from, to ->
                onEvent(MainScreenEvent.OnMoveTodo(from.index, to.index))
            }
        )

    var previousTodosSize by remember { mutableIntStateOf(uiState.todos.size) }

    LaunchedEffect(uiState.todos.size) {
        if (uiState.todos.size > previousTodosSize) {
            lazyListState.animateScrollToItem(0)
        }
        previousTodosSize = uiState.todos.size
    }

    BackHandler(uiState.isSelectionMode) {
        onEvent(MainScreenEvent.ClearSelection)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager)
    ) {
        if (uiState.isSelectionMode) {
            TopAppBar(
                title = { Text(text = "${uiState.selectedTodoIds.size} selected") },
                navigationIcon = {
                    IconButton(onClick = { onEvent(MainScreenEvent.ClearSelection) }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear selection")
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(MainScreenEvent.DeleteSelectedTodos) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete selected")
                    }
                }
            )
        } else {
            MainTodoTopAppBar(
                title = "DEEP.FINE TODO",
                onNavigationClick = {
                    onEvent(MainScreenEvent.OnNavigate)
                }
            )
        }

        if (uiState.todos.isEmpty()) {
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
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
                state = lazyListState,
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.todos, key = { it.id }) { todo ->
                    ReorderableItem(
                        state = state,
                        key = todo.id,
                        enabled = !uiState.isSelectionMode && swipingTodoId == null
                    ) { isDragging ->
                        val elevation = if (isDragging) 16.dp else 0.dp
                        val alpha = if (isDragging) 0.5f else 1f
                        SwipeableTodoItem(
                            modifier = Modifier
                                .graphicsLayer(alpha = alpha)
                                .clickable {
                                    onEvent(MainScreenEvent.OnTodoSelectionClick(todo.id))
                                }
                                .longPressDraggableHandle(enabled = !uiState.isSelectionMode),
                            todo = todo,
                            onToggleComplete = { onEvent(MainScreenEvent.ToggleTodoComplete(it)) },
                            onDelete = { onEvent(MainScreenEvent.DeleteTodo(it)) },
                            isSelected = todo.id in uiState.selectedTodoIds,
                            gestureEnabled = !uiState.isSelectionMode && !isDragging,
                            onSwipeStateChange = { swipingTodoId = if (it) todo.id else null }
                        )
                    }
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