package com.tedd.todo_project.designsystem.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryColor,
    inversePrimary = Positive,
    secondary = SecondaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    error = WarningColor
)

@Composable
fun TODO_ProjectTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme

    val view = LocalView.current
    val window = (view.context as? Activity)?.window

    window?.let {
        WindowCompat.setDecorFitsSystemWindows(it, false)
        WindowCompat
            .getInsetsController(it, view)
            .apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
