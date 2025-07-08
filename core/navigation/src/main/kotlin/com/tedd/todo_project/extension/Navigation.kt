package com.tedd.todo_project.extension

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.tedd.todo_project.navigation.Route

inline fun <reified T : Route> NavController.hasRouteNavigateUp() {
    if (currentDestination?.hasRoute<T>() != true) {
        navigateUp()
    }
}