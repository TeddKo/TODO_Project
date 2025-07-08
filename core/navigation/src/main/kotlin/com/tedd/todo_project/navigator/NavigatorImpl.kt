package com.tedd.todo_project.navigator

import com.tedd.todo_project.navigation.Route
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigatorImpl @Inject constructor() : Navigator, ChannelNavigator {
    override val channel: Channel<IntentRoute> = Channel<IntentRoute>(Channel.BUFFERED)

    override suspend fun navigate(
        route: Route,
        saveState: Boolean,
        launchSingleTop: Boolean
    ) {
        println("NavigatorImpl: navigate called. Sending route to channel: $route")
        channel.send(
            IntentRoute.Navigate(
                route = route,
                saveState = saveState,
                launchSingleTop = launchSingleTop
            )
        )
    }

    override suspend fun navigateBack() {
        channel.send(IntentRoute.NavigateBack)
    }

}
