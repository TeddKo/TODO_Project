package com.tedd.todo_project.main.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tedd.todo_project.designsystem.theme.BorderColor
import com.tedd.todo_project.designsystem.theme.PrimaryColor
import com.tedd.todo_project.designsystem.theme.TODO_ProjectTheme
import com.tedd.todo_project.designsystem.theme.WarningColor
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
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete(todo)
                true
            } else {
                false
            }
        }
    )

    // Observe dismissState changes to notify parent about swipe state
    val isSwiping by remember {
        derivedStateOf {
            dismissState.currentValue != SwipeToDismissBoxValue.Settled || dismissState.targetValue != SwipeToDismissBoxValue.Settled
        }
    }

    SideEffect {
        onSwipeStateChange(isSwiping)
    }
    SwipeToDismissBox(
        modifier = Modifier.shadow(elevation = elevation),
        state = dismissState,
        enableDismissFromStartToEnd = false,
        gesturesEnabled = gestureEnabled,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> WarningColor
                    else -> BorderColor
                },
                label = "DismissColorAnimation"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 22.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "삭제",
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
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { onToggleComplete(todo.copy(isCompleted = !todo.isCompleted)) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .border(width = 1.dp, color = PrimaryColor, shape = CircleShape)
                            .padding(3.dp),
                        imageVector = Icons.Default.Check,
                        contentDescription = "Check",
                        tint = PrimaryColor
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = todo.work,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = Int.MAX_VALUE,
                        overflow = TextOverflow.Visible
                    )
                }
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