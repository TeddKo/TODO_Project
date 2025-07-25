package com.tedd.todo_project.histories.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tedd.todo_project.core.designsystem.R
import com.tedd.todo_project.designsystem.theme.SurfaceColor
import com.tedd.todo_project.histories.util.toFormattedString
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month

@Composable
fun HistoriesColumn(
    todoText: String,
    backgroundColor: Color,
    registrationDate: LocalDateTime,
    completionDate: LocalDateTime?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor, shape = RoundedCornerShape(10.dp))
            .clip(shape = RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = todoText,
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.registration_date, registrationDate.toFormattedString()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            completionDate?.let {
                Text(
                    text = stringResource(R.string.completion_date, it.toFormattedString()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoriesColumnPreview() {
    HistoriesColumn(
        todoText = "Sample Todo Item",
        backgroundColor = Color.Unspecified,
        registrationDate = LocalDateTime(2023, Month.JANUARY, 1, 10, 0, 0),
        completionDate = LocalDateTime(2023, Month.JANUARY, 1, 11, 0, 0)
    )
}

@Preview(showBackground = true)
@Composable
fun HistoriesColumnPreviewNoCompletion() {
    HistoriesColumn(
        todoText = "Another Sample Todo Item (Not Completed)",
        backgroundColor = Color.Unspecified,
        registrationDate = LocalDateTime(2023, Month.FEBRUARY, 15, 9, 30, 0),
        completionDate = null
    )
}