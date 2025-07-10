package com.tedd.todo_project.main.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tedd.todo_project.histories.HistoriesScreen
import com.tedd.todo_project.histories.viewmodel.HistoriesViewModel
import com.tedd.todo_project.main.MainScreen
import com.tedd.todo_project.main.viewmodel.MainViewModel
import com.tedd.todo_project.navigation.Route

fun NavGraphBuilder.mainNavGraph(mainViewModel: MainViewModel) {

    composable<Route.Main> {
        MainScreen(
            uiState = { mainViewModel.uiState },
            onEvent = mainViewModel::onEvent
        )
    }

    composable<Route.History> {
        val historiesViewModel: HistoriesViewModel = hiltViewModel()
        HistoriesScreen(
            uiState = { historiesViewModel.uiState }
        )
    }
}