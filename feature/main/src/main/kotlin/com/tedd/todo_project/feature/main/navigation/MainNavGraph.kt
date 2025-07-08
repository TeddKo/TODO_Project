package com.tedd.todo_project.feature.main.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tedd.todo_project.feature.histories.HistoriesScreen
import com.tedd.todo_project.feature.histories.viewmodel.HistoriesViewModel
import com.tedd.todo_project.feature.main.MainScreen
import com.tedd.todo_project.feature.main.viewmodel.MainViewModel
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