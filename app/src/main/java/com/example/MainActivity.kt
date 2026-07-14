package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ui.screen.AddServiceScreen
import com.example.ui.screen.HomeScreen
import com.example.ui.screen.ServiceDetailScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.DirectoryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Retrieve or create ViewModel with Factory containing application context
            val directoryViewModel: DirectoryViewModel = viewModel(
                factory = DirectoryViewModel.Factory(application)
            )

            val userDarkThemeSelection by directoryViewModel.isDarkTheme.collectAsState(initial = null)
            val darkTheme = userDarkThemeSelection ?: androidx.compose.foundation.isSystemInDarkTheme()

            MyApplicationTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "dashboard",
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 1. Dashboard / Main Directory Screen
                    composable("dashboard") {
                        HomeScreen(
                            viewModel = directoryViewModel,
                            onNavigateToDetail = { serviceId ->
                                navController.navigate("service_detail/$serviceId")
                            },
                            onNavigateToAddService = {
                                navController.navigate("add_service")
                            }
                        )
                    }

                    // 2. Add New Listing Screen
                    composable("add_service") {
                        AddServiceScreen(
                            viewModel = directoryViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    // 3. Service Detail Screen with Argument Parsing
                    composable(
                        route = "service_detail/{serviceId}",
                        arguments = listOf(
                            navArgument("serviceId") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val serviceId = backStackEntry.arguments?.getInt("serviceId") ?: 0
                        ServiceDetailScreen(
                            serviceId = serviceId,
                            viewModel = directoryViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
