package com.example.location_journal.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.location_journal.ProfileScreen
import com.example.location_journal.viewmodel.JournalViewModel


@Composable
fun Navigation(navController: NavHostController, viewModel: JournalViewModel) {
    NavHost(navController = navController, startDestination = "journal") {
        composable("journal") { JournalEntryScreen(viewModel = viewModel) }
        composable("heatmap") { HeatMapScreen(viewModel = viewModel) }
        composable("profile") { ProfileScreen(viewModel = viewModel) }
    }
}