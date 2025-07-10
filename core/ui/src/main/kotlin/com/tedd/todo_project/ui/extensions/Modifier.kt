package com.tedd.todo_project.ui.extensions

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.addFocusCleaner(
    focusManager: FocusManager
) = this.pointerInput(Unit) {
    detectTapGestures(
        onTap = {
            focusManager.clearFocus()
        }
    )
}