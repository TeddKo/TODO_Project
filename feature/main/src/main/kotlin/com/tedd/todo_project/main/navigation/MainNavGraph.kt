package com.tedd.todo_project.main.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.tedd.todo_project.histories.HistoriesScreen
import com.tedd.todo_project.histories.viewmodel.HistoriesViewModel
import com.tedd.todo_project.main.MainScreen
import com.tedd.todo_project.main.viewmodel.MainViewModel
import com.tedd.todo_project.route.Route

fun NavGraphBuilder.mainNavGraph() {

    composable<Route.Main> {
        val viewModel: MainViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        MainScreen(
            uiState = uiState,
            onIntent = viewModel::onIntent
        )
    }

    composable<Route.History> {
        val viewModel: HistoriesViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        HistoriesScreen(
            uiState = uiState,
            onIntent = viewModel::onIntent
        )
    }
}