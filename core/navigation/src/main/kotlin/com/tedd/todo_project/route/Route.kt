package com.tedd.todo_project.route

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Main : Route

    @Serializable
    data object History : Route
}