package com.tedd.todo_project.navigator

import com.tedd.todo_project.route.Route
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor() : Navigator, NavigationChannelProvider {
    override val channel: Channel<NavigationIntent> = Channel(Channel.BUFFERED)

    override suspend fun navigate(
        route: Route,
        saveState: Boolean,
        launchSingleTop: Boolean
    ) {
        channel.send(
            NavigationIntent.Navigate(
                route = route,
                saveState = saveState,
                launchSingleTop = launchSingleTop
            )
        )
    }

    override suspend fun navigateBack() {
        channel.send(NavigationIntent.NavigateBack)
    }
}
