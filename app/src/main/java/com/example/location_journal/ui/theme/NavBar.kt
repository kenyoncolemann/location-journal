package com.example.location_journal.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// navigation bar items
data class NavItem(val title: String, val icon: ImageVector, val route: String)

// list of all nav bar items
val navItems = listOf(
    NavItem("Journal", Icons.Default.Edit, "journal"),
    NavItem("Heatmap", Icons.Default.Place, "heatmap"),
    NavItem("Profile", Icons.Default.Person, "profile")
)

// renders the bottom naviagtion bar
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