package com.example.location_journal.ui.theme


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.location_journal.data.UserDatabase
import com.example.location_journal.viewmodel.UserViewModel

// sets up navigation for login and registration
@Composable
fun AuthNavHost(
    userViewModel: UserViewModel,
    onLoginSuccess: () -> Unit
) {
    val navController = rememberNavController()
    val context: Context = LocalContext.current
    val userDao = UserDatabase.getDatabase(context).userDao()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(userDao = userDao, onLogin = { user ->
                userViewModel.currentUser = user
                onLoginSuccess()
            }, onNavigateToRegister = {
                navController.navigate("register")
            })
        }

        composable("register") {
            RegistrationScreen(userDao = userDao, onRegistered = {
                navController.popBackStack("login", inclusive = false)
            })
        }
    }
}
