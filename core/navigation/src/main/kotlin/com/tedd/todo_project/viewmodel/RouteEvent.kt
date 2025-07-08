package com.tedd.todo_project.viewmodel

import com.tedd.todo_project.navigation.Route

sealed interface RouteEvent {

    data class Navigate(
        val route: Route,
        val saveState: Boolean,
        val launchSingleTop: Boolean,
    ) : RouteEvent


    data object NavigateBack : RouteEvent
}
