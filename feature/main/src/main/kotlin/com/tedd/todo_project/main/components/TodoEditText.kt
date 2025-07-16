package com.tedd.todo_project.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tedd.todo_project.core.designsystem.R
import com.tedd.todo_project.designsystem.theme.BorderColor
import com.tedd.todo_project.designsystem.theme.PrimaryColor
import com.tedd.todo_project.designsystem.theme.SecondaryColor
import com.tedd.todo_project.designsystem.theme.TODO_ProjectTheme

@Composable
fun TodoEditText(
    modifier: Modifier = Modifier,
    isUpdatableWork: Boolean,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onAddTodo: (String) -> Unit,
    onCancel: () -> Unit
) {
    val isButtonEnabled = value.text.isNotBlank()
    var isFocused by remember { mutableStateOf(false) }

    val animateButtonAlpha by animateFloatAsState(
        targetValue = if (isButtonEnabled) 1f else 0.3f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "buttonAlphaAnimation"
    )
    val animateBorderAlpha by animateFloatAsState(
        targetValue = if (isFocused) 1f else 0.3f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "borderColorAnimation"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp)
            .navigationBarsPadding()
            .imePadding()
    ) {
        AnimatedVisibility(visible = isUpdatableWork) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(imageVector = Icons.Default.EditNote, contentDescription = "edit_work")
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 6.dp),
                    text = stringResource(R.string.selected_edit)
                )

                IconButton(onClick = onCancel) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "cancel")
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
        ) {
            TextField(
                modifier = Modifier
                    .onFocusChanged { isFocused = it.isFocused }
                    .weight(1f)
                    .border(
                        width = 1.dp,
                        color = BorderColor.copy(alpha = animateBorderAlpha),
                        shape = RoundedCornerShape(26.dp)
                    ),
                value = value,
                onValueChange = onValueChange,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(21.dp),
                singleLine = true,
                placeholder = { Text(text = stringResource(R.string.input_work)) }
            )

            IconButton(
                onClick = { onAddTodo(value.text) },
                enabled = isButtonEnabled
            ) {
                Icon(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = SecondaryColor.copy(alpha = animateButtonAlpha),
                            shape = CircleShape
                        )
                        .padding(5.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = "Add Todo",
                    tint = PrimaryColor.copy(alpha = animateButtonAlpha)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoEditText() {
    TODO_ProjectTheme {
        TodoEditText(
            isUpdatableWork = true,
            value = TextFieldValue("Sample Todo"),
            onValueChange = {},
            onAddTodo = {},
            onCancel = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTodoEditTextEmpty() {
    val (textFieldValue, onTextChange) = remember { mutableStateOf(TextFieldValue("")) }
    TODO_ProjectTheme {
        TodoEditText(
            isUpdatableWork = false,
            value = textFieldValue,
            onValueChange = onTextChange,
            onAddTodo = {},
            onCancel = {}
        )
    }
}
