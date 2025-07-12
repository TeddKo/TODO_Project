package com.tedd.todo_project.main.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tedd.todo_project.core.designsystem.R
import com.tedd.todo_project.designsystem.theme.BorderColor
import com.tedd.todo_project.designsystem.theme.TODO_ProjectTheme
import com.tedd.todo_project.domain.model.Todo
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableTodoItem(
    modifier: Modifier = Modifier,
    todo: Todo,
    backgroundColor: Color,
    elevation: Dp,
    onToggleComplete: (Todo) -> Unit,
    onDelete: (Todo) -> Unit,
    isSelected: Boolean,
    gestureEnabled: Boolean,
    onSwipeStateChange: (Boolean) -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.StartToEnd) {
                onToggleComplete(todo.copy(isCompleted = !todo.isCompleted))
            } else if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete(todo)
            }
            true
        }
    )

    val isSwiping by remember {
        derivedStateOf {
            dismissState.currentValue != SwipeToDismissBoxValue.Settled || dismissState.targetValue != SwipeToDismissBoxValue.Settled
        }
    }

    SideEffect { onSwipeStateChange(isSwiping) }

    SwipeToDismissBox(
        modifier = Modifier.shadow(elevation = elevation),
        state = dismissState,
        gesturesEnabled = gestureEnabled,
        backgroundContent = {

            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.inversePrimary
                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                    SwipeToDismissBoxValue.Settled -> BorderColor
                },
                animationSpec = tween(durationMillis = 300, easing = LinearEasing)
            )

            val (alignment, text) = when (dismissState.targetValue) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart to stringResource(R.string.complete)
                SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd to stringResource(R.string.delete)
                SwipeToDismissBoxValue.Settled -> Alignment.CenterStart to ""
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 22.dp),
                contentAlignment = alignment
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }
        },
        content = {
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) MaterialTheme.colorScheme.secondary else backgroundColor,
                animationSpec = tween(durationMillis = 300, easing = LinearEasing)
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = backgroundColor)
                    .padding(horizontal = 22.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = todo.work,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = Int.MAX_VALUE,
                    overflow = TextOverflow.Visible
                )
            }
        }
    )
}

@Preview
@Composable
fun PreviewSwipeableTodoItem() {
    TODO_ProjectTheme {
        SwipeableTodoItem(
            todo = Todo(
                id = 1L,
                work = "This is a sample todo item",
                isCompleted = false,
                addedTime = LocalDateTime(2024, 7, 10, 10, 0, 0),
                completedTime = null,
                position = 0
            ),
            backgroundColor = BorderColor,
            elevation = 0.dp,
            onToggleComplete = {},
            onDelete = {},
            isSelected = false,
            gestureEnabled = false,
            onSwipeStateChange = { }
        )
    }
}