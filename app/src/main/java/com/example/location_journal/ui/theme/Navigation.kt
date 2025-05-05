package com.example.location_journal.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable



@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "journal") {
        composable("journal") { JournalEntryScreen() }
        composable("heatmap") { HeatMapScreen() }
        composable("profile") { ProfileScreen() }
    }
}