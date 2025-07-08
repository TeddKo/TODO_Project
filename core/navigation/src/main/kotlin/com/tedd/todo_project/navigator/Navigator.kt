package com.tedd.todo_project.navigator

import com.tedd.todo_project.navigation.Route

interface Navigator {
    suspend fun navigate(
        route: Route,
        saveState: Boolean = false,
        launchSingleTop: Boolean = false
    )

    suspend fun navigateBack()
}