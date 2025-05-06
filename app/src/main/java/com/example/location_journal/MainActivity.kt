package com.example.location_journal

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.example.location_journal.ui.theme.*
import com.example.location_journal.viewmodel.JournalViewModel
import com.example.location_journal.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    private val journalViewModel: JournalViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            LocationJournalTheme {
                val navController = rememberNavController()

                var loggedIn by remember { mutableStateOf(false) }

                if (!loggedIn && userViewModel.currentUser == null) {
                    AuthNavHost(userViewModel = userViewModel) {
                        loggedIn = true // ✅ Just flip a flag — no nav
                    }
                } else {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            NavBar(navController = navController)
                        }
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            Navigation(
                                navController = navController,
                                viewModel = journalViewModel,
                                currentUser = userViewModel.currentUser!!
                            )
                        }
                    }
                }
            }
        }
    }
}
