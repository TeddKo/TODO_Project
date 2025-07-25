package com.tedd.todo_project.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tedd.todo_project.core.designsystem.R
import com.tedd.todo_project.designsystem.theme.BorderColor
import com.tedd.todo_project.designsystem.theme.TODO_ProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableTodoItem(
    modifier: Modifier = Modifier.Companion,
    cornerRadius: Dp = 0.dp,
    elevation: Dp,
    onToggleComplete: (() -> Unit)? = null,
    onDelete: () -> Unit,
    clickEnabled: Boolean = true,
    onClick: () -> Unit,
    isGestured: Boolean,
    isSelected: Boolean,
    isDragging: Boolean = false,
    onDismissStateChanged: (SwipeToDismissBoxValue) -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    if (onToggleComplete != null) {
                        onToggleComplete()
                    }
                    true
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    true
                }

                SwipeToDismissBoxValue.Settled -> false
            }
        },
        positionalThreshold = { it * .5f }
    )
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animateScale by animateFloatAsState(
        targetValue = when {
            (isPressed && clickEnabled) || isSelected -> .95f
            (isPressed && clickEnabled) || isDragging -> 1.05f
            else -> 1f
        },
        animationSpec = spring()
    )

    LaunchedEffect(dismissState.dismissDirection) {
        onDismissStateChanged(dismissState.dismissDirection)
    }

    SwipeToDismissBox(
        modifier = modifier
            .scale(scale = animateScale)
            .shadow(elevation = elevation)
            .clip(shape = RoundedCornerShape(size = cornerRadius))
            .clickable(
                enabled = clickEnabled,
                interactionSource = interactionSource,
                indication = ripple(),
                onClick = onClick
            ),
        state = dismissState,
        enableDismissFromStartToEnd = onToggleComplete != null,
        gesturesEnabled = isGestured,
        backgroundContent = {

            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.inversePrimary
                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                SwipeToDismissBoxValue.Settled -> BorderColor
            }

            val (alignment, text) = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.Companion.CenterStart to stringResource(
                    R.string.complete
                )

                SwipeToDismissBoxValue.EndToStart -> Alignment.Companion.CenterEnd to stringResource(
                    R.string.delete
                )

                SwipeToDismissBoxValue.Settled -> Alignment.Companion.CenterStart to ""
            }
            Box(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 22.dp),
                contentAlignment = alignment
            ) {
                Text(
                    text = text,
                    color = Color.Companion.White,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Companion.Center
                )
            }
        },
        content = content
    )
}

@Preview
@Composable
fun PreviewSwipeableTodoItem() {
    TODO_ProjectTheme {
        SwipeableTodoItem(
            elevation = 0.dp,
            onToggleComplete = {},
            onDelete = {},
            onClick = {},
            isGestured = false,
            isSelected = false,
            onDismissStateChanged = { },
            content = {}
        )
    }
}