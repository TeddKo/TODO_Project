package com.tedd.todo_project.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LibraryAddCheck
import androidx.compose.material.icons.outlined.LibraryAddCheck
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tedd.todo_project.designsystem.theme.TODO_ProjectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text(
                text = title,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
        },
        navigationIcon = navigationIcon ?: {},
        actions = actions ?: {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = Color.Unspecified,
            navigationIconContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionDeleteTopAppBar(
    title: String,
    isAllSelected: Boolean,
    onClearSelection: () -> Unit,
    onSelectAll: () -> Unit,
    onDeleteSelected: () -> Unit
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            Row {
                IconButton(onClick = onClearSelection) {
                    Icon(Icons.Default.Close, contentDescription = "Clear selection")
                }
                IconButton(onClick = onSelectAll) {
                    val imageVector =
                        if (isAllSelected) Icons.Default.LibraryAddCheck else Icons.Outlined.LibraryAddCheck
                    Icon(imageVector = imageVector, contentDescription = "All Select")
                }
            }
        },
        actions = {
            IconButton(onClick = onDeleteSelected) {
                Icon(Icons.Default.Delete, contentDescription = "Delete selected")
            }
        }
    )
}

@Composable
fun MainTodoTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavigationClick: () -> Unit
) {
    TodoTopAppBar(
        modifier = modifier,
        title = title,
        actions = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    imageVector = Icons.Default.History,
                    contentDescription = "history_icon",
                )
            }
        }
    )
}

@Composable
fun HistoriesTopAppBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavigationClick: () -> Unit
) {
    TodoTopAppBar(
        modifier = modifier,
        title = title,
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "arrow_back_icon",
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewMainTodoTopAppBar() {
    TODO_ProjectTheme {
        MainTodoTopAppBar(title = "DEEP.FINE TODO") { }
    }
}

@Preview
@Composable
private fun PreviewHistoriesTopAppBar() {
    TODO_ProjectTheme {
        HistoriesTopAppBar(title = "History") { }
    }
}
