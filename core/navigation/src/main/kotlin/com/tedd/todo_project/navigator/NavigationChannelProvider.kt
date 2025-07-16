package com.tedd.todo_project.navigator

import kotlinx.coroutines.channels.Channel

interface NavigationChannelProvider {
    val channel: Channel<NavigationIntent>
}