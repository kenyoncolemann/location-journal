package com.example.location_journal.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.location_journal.ProfileScreen
import com.example.location_journal.viewmodel.JournalViewModel
import com.example.location_journal.data.UserEntryItem

@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: JournalViewModel,
    currentUser: UserEntryItem // ✅ Accept the user passed from MainActivity
) {
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
