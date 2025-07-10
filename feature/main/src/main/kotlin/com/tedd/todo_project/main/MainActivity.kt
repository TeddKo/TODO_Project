package com.tedd.todo_project.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.tedd.todo_project.LaunchedRouter
import com.tedd.todo_project.designsystem.theme.TODO_ProjectTheme
import com.tedd.todo_project.main.navhost.MainNavHost
import com.tedd.todo_project.main.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val navController = rememberNavController()

            LaunchedRouter(navHostController = navController)

            TODO_ProjectTheme {
                MainNavHost(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}