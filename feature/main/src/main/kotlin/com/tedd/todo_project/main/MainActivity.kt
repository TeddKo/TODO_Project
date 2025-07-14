package com.tedd.todo_project.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.tedd.todo_project.LaunchedRouter
import com.tedd.todo_project.designsystem.theme.TODO_ProjectTheme
import com.tedd.todo_project.main.navhost.MainNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
        }
        setContent {
            val navController = rememberNavController()

            LaunchedRouter(navHostController = navController)

            TODO_ProjectTheme {
                MainNavHost(navController = navController)
            }
        }
    }
}