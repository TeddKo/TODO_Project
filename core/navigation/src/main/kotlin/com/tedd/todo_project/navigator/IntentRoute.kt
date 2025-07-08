package com.tedd.todo_project.navigator

import com.tedd.todo_project.navigation.Route

sealed interface IntentRoute {

    data class Navigate(
        val route: Route,
        val saveState: Boolean,
        val launchSingleTop: Boolean,
    ): IntentRoute

    data object NavigateBack: IntentRoute
}