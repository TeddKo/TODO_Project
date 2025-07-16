package com.tedd.todo_project.navigator

import com.tedd.todo_project.route.Route

sealed interface NavigationIntent {
    data class Navigate(
        val route: Route,
        val saveState: Boolean,
        val launchSingleTop: Boolean,
    ) : NavigationIntent
    data object NavigateBack : NavigationIntent
}