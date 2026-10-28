package com.planner.persian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.planner.persian.ui.navigation.AppNavGraph
import com.planner.persian.ui.theme.PersianPlannerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as PersianPlannerApp
        val repository = app.repository

        setContent {
            PersianPlannerTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    repository = repository
                )
            }
        }
    }
}
