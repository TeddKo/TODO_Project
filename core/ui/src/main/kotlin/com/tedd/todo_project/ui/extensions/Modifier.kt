package com.tedd.todo_project.ui.extensions

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope

@Composable
fun Modifier.addFocusCleaner(focusManager: FocusManager) = this.pointerInput(Unit) {
    detectTapGestures(
        onTap = {
            focusManager.clearFocus()
        }
    )
}

fun Modifier.disableSplitMotionEvents() =
    this.pointerInput(Unit) {
        coroutineScope {
            var currentId: Long = -1L
            awaitPointerEventScope {
                while (true) {
                    awaitPointerEvent(PointerEventPass.Initial).changes.forEach { pointerInfo ->
                        when {
                            pointerInfo.pressed && currentId == -1L -> currentId =
                                pointerInfo.id.value

                            pointerInfo.pressed.not() && currentId == pointerInfo.id.value -> currentId =
                                -1

                            pointerInfo.id.value != currentId && currentId != -1L -> pointerInfo.consume()
                            else -> Unit
                        }
                    }
                }
            }
        }
    }