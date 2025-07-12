package com.tedd.todo_project.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material.icons.outlined.LibraryAddCheck
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.tedd.todo_project.core.designsystem.R
import com.tedd.todo_project.main.components.SwipeableTodoItem
import com.tedd.todo_project.main.components.TodoEditText
import com.tedd.todo_project.main.viewmodel.MainScreenIntent
import com.tedd.todo_project.main.viewmodel.MainScreenState
import com.tedd.todo_project.ui.components.MainTodoTopAppBar
import com.tedd.todo_project.ui.extensions.addFocusCleaner
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    uiState: MainScreenState,
    onIntent: (MainScreenIntent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val lazyListState = rememberLazyListState()
    val state =
        rememberReorderableLazyListState(
            lazyListState = lazyListState,
            onMove = { from, to ->
                onIntent(MainScreenIntent.OnMoveTodo(from.index, to.index))
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
        onIntent(MainScreenIntent.ClearSelection)
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(alignment = Alignment.Center),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .addFocusCleaner(focusManager)
        ) {
            if (uiState.isSelectionMode) {
                TopAppBar(
                    title = { Text(text = "${uiState.selectedTodoIds.size} selected") },
                    navigationIcon = {
                        Row {
                            IconButton(onClick = { onIntent(MainScreenIntent.ClearSelection) }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear selection")
                            }
                            IconButton(onClick = { onIntent(MainScreenIntent.OnSelectAllTodos) }) {
                                val isAllSelected =
                                    uiState.todos.isNotEmpty() && uiState.selectedTodoIds.size == uiState.todos.size
                                val imageVector =
                                    if (isAllSelected) Icons.Default.LibraryAddCheck else Icons.Outlined.LibraryAddCheck
                                Icon(imageVector = imageVector, contentDescription = "All Select")
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { onIntent(MainScreenIntent.DeleteSelectedTodos) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete selected")
                        }
                    }
                )
            } else {
                MainTodoTopAppBar(
                    title = stringResource(R.string.todo),
                    onNavigationClick = {
                        onIntent(MainScreenIntent.OnNavigate)
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
                        text = stringResource(R.string.empty_todos)
                    )
                }
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(uiState.todos, key = { it.id }) { todo ->
                        ReorderableItem(
                            state = state,
                            key = todo.id,
                            enabled = !uiState.isSelectionMode && uiState.swipingTodoId == null
                        ) { isDragging ->
                            val elevation by animateDpAsState(
                                targetValue = if (isDragging) 16.dp else 0.dp,
                                animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                            )
                            val backgroundColor by animateColorAsState(
                                targetValue = if (isDragging) Color.LightGray else MaterialTheme.colorScheme.surface,
                                animationSpec = tween(durationMillis = 300, easing = LinearEasing)
                            )
                            SwipeableTodoItem(
                                modifier = Modifier
                                    .clickable {
                                        onIntent(MainScreenIntent.OnTodoSelectionClick(todo.id))
                                    }
                                    .longPressDraggableHandle(
                                        enabled = !uiState.isSelectionMode,
                                        onDragStopped = { onIntent(MainScreenIntent.OnUpdateTodos) }
                                    ),
                                todo = todo,
                                backgroundColor = backgroundColor,
                                elevation = elevation,
                                onToggleComplete = { onIntent(MainScreenIntent.ToggleTodoComplete(it)) },
                                onDelete = { onIntent(MainScreenIntent.DeleteTodo(it)) },
                                isSelected = todo.id in uiState.selectedTodoIds,
                                gestureEnabled = !uiState.isSelectionMode && !isDragging,
                                onSwipeStateChange = { isSwiping ->
                                    onIntent(
                                        MainScreenIntent.OnSwipeStateChange(
                                            todoId = todo.id,
                                            isSwiping = isSwiping
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

            TodoEditText(
                modifier = Modifier.fillMaxWidth(),
                text = uiState.todoInput,
                onTextChange = { onIntent(MainScreenIntent.UpdateTodoInput(it)) },
                onAddTodo = { onIntent(MainScreenIntent.AddTodo) }
            )
        }
    }

}