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
import android.Manifest

class MainActivity : ComponentActivity() {
    // view models to manage app state
    private val journalViewModel: JournalViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // check for location permissions, request if not
        if (hasLocationPermissions()) {
            launchAppContent()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }
    // checks whether location permissions have been granted
    private fun hasLocationPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // launches the app content
    private fun launchAppContent() {
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

    // handles the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            launchAppContent() // launch ui
        } else {
            // could exit or show error
        }
    }

    //
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
