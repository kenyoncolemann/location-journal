package com.example.location_journal.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.location_journal.ProfileScreen
import com.example.location_journal.viewmodel.JournalViewModel
import com.example.location_journal.data.UserEntryItem

// navigation between main screens
@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: JournalViewModel,
    currentUser: UserEntryItem // logged in user
) {
    // manages which screen is shown based on the current route
    NavHost(navController = navController, startDestination = "journal") {
        composable("journal") {
            JournalEntryScreen(viewModel = viewModel, user = currentUser)
        }
        composable("heatmap") {
            HeatMapScreen(viewModel = viewModel, user = currentUser)
        }
        composable("profile") {
            ProfileScreen(viewModel = viewModel, user = currentUser)
        }
    }
}
