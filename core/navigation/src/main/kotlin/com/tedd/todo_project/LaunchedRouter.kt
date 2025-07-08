package com.tedd.todo_project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.UriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.tedd.todo_project.extension.hasRouteNavigateUp
import com.tedd.todo_project.navigation.Route
import com.tedd.todo_project.viewmodel.RouteEvent
import com.tedd.todo_project.viewmodel.RouterViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LaunchedRouter(navHostController: NavHostController) {
    InternalLaunchedRouter(navHostController = navHostController)
}

@Composable
private fun InternalLaunchedRouter(
    navHostController: NavHostController,
    routerViewModel: RouterViewModel = hiltViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(routerViewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            routerViewModel.event.collectLatest { even ->
                when (even) {
                    is RouteEvent.NavigateBack -> {
                        navHostController.hasRouteNavigateUp<Route.Main>()
                    }

                    is RouteEvent.Navigate -> {
                        navHostController.navigate(even.route) {
                            if (even.saveState) {
                                navHostController.graph.findStartDestination().route?.let {
                                    popUpTo(it) {
                                        saveState = true
                                    }
                                }
                                restoreState = true
                            }
                            launchSingleTop = even.launchSingleTop
                        }
                    }
                }
            }
        }
    }
}