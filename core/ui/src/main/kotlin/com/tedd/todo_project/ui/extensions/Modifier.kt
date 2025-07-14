package com.tedd.todo_project.ui.extensions

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
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

fun Modifier.disableSplitMotionEvents() = this.pointerInput(Unit) {
    handlePointerEvents()
}

private suspend fun PointerInputScope.handlePointerEvents() {
    coroutineScope {
        val pointerTracker = PointerTracker()

        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Initial)

                pointerTracker.processPointerChanges(event.changes)
            }
        }
    }
}

private class PointerTracker {
    private var currentId: Long = -1L
    fun processPointerChanges(changes: List<PointerInputChange>) {
        changes.forEach { pointerInfo ->
            when {
                pointerInfo.pressed && currentId == -1L -> {
                    currentId = pointerInfo.id.value
                }

                !pointerInfo.pressed && currentId == pointerInfo.id.value -> {
                    currentId = -1L
                }

                pointerInfo.id.value != currentId && currentId != -1L -> {
                    pointerInfo.consume()
                }
            }
        }
    }
}