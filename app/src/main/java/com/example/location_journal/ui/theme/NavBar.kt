package com.example.location_journal.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class NavItem(val title: String, val icon: ImageVector, val route: String)

val navItems = listOf(
    NavItem("Journal", Icons.Default.Edit, "journal"),
    NavItem("Heatmap", Icons.Default.Place, "heatmap"),
    NavItem("Profile", Icons.Default.Person, "profile")
)

@Composable
fun NavBar(navController: NavController) {
    NavigationBar {
        val navEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navEntry?.destination?.route

        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}