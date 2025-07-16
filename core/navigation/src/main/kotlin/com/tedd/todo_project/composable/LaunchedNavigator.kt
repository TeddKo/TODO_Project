package com.tedd.todo_project.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.tedd.todo_project.navigator.NavigationIntent
import com.tedd.todo_project.viewmodel.RouterViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LaunchedNavigator(
    navHostController: NavHostController,
    finish: () -> Unit
) {
    LaunchedNavigator(
        navHostController = navHostController,
        routerViewModel = hiltViewModel(),
        finish = finish
    )
}

@Composable
private fun LaunchedNavigator(
    navHostController: NavHostController,
    routerViewModel: RouterViewModel,
    finish: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(routerViewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            routerViewModel
                .intent
                .collectLatest { intent ->
                    when (intent) {
                        is NavigationIntent.Navigate -> {
                            navHostController.navigate(intent.route) {
                                if (intent.saveState) {
                                    navHostController.graph.findStartDestination().route?.let {
                                        popUpTo(it) {
                                            saveState = true
                                        }
                                    }
                                    restoreState = true
                                }
                                launchSingleTop = intent.launchSingleTop
                            }
                        }

                        NavigationIntent.NavigateBack -> {
                            val previousBackStackEntry = navHostController.previousBackStackEntry
                            if (previousBackStackEntry != null) {
                                navHostController.navigateUp()
                            } else finish()
                        }
                    }
                }
        }
    }
}