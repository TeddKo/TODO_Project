package com.tedd.todo_project.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.tedd.todo_project.composable.LaunchedNavigator
import com.tedd.todo_project.designsystem.theme.TODO_ProjectTheme
import com.tedd.todo_project.main.navhost.MainNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val navController = rememberNavController()

            LaunchedNavigator(
                navHostController = navController,
                finish = ::finish
            )

            TODO_ProjectTheme {
                MainNavHost(navController = navController)
            }
        }
    }
}