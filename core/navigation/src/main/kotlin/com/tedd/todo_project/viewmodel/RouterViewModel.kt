package com.tedd.todo_project.viewmodel

import androidx.lifecycle.ViewModel
import com.tedd.todo_project.navigator.ChannelNavigator
import com.tedd.todo_project.navigator.IntentRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class RouterViewModel @Inject constructor(
    navigator: ChannelNavigator
) : ViewModel() {
    val event = navigator
            .channel
            .receiveAsFlow()
            .map { router ->
                when (router) {
                    is IntentRoute.Navigate -> {
                        RouteEvent.Navigate(
                            route = router.route,
                            saveState = router.saveState,
                            launchSingleTop = router.launchSingleTop
                        )
                    }
                    is IntentRoute.NavigateBack -> RouteEvent.NavigateBack
                }

    }
}
