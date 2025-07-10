package com.tedd.todo_project.main.navhost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.tedd.todo_project.designsystem.theme.BackgroundColor
import com.tedd.todo_project.main.navigation.mainNavGraph
import com.tedd.todo_project.main.viewmodel.MainViewModel
import com.tedd.todo_project.navigation.Route

@Composable
internal fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        NavHost(
            navController = navController,
            startDestination = Route.Main,
        ) {
            mainNavGraph(mainViewModel = viewModel)
        }
    }
}