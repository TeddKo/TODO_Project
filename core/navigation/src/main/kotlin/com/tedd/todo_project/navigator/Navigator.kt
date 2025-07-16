package com.tedd.todo_project.navigator

import com.tedd.todo_project.route.Route

interface Navigator {
    suspend fun navigate(
        route: Route,
        saveState: Boolean = false,
        launchSingleTop: Boolean = false
    )

    suspend fun navigateBack()
}