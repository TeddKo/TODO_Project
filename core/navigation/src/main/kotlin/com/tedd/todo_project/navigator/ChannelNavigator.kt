package com.tedd.todo_project.navigator

import kotlinx.coroutines.channels.Channel

interface ChannelNavigator {
    val channel: Channel<IntentRoute>
}