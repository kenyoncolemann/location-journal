package com.example.location_journal.ui.theme

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.location_journal.data.JournalEntryItem
import com.example.location_journal.viewmodel.JournalViewModel
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("MissingPermission")
@Composable
fun JournalEntryScreen(viewModel: JournalViewModel) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Fetching location...") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        getLocation(context) { loc ->
            location = "${loc.latitude}, ${loc.longitude}"
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("New Journal Entry", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Write your thoughts...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
                    val entry = JournalEntryItem(
                        date = currentDate,
                        mood = "", // analysis func
                        text = text,
                        location = location
                    )
                    viewModel.addEntry(entry)
                    text = ""

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Entry saved!")
                    }
                },
                modifier = Modifier.align(Alignment.End),
                enabled = text.isNotBlank()
            ) {
                Text("Save Entry")
            }
        }
    }
}

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun getLocation(context: Context, onLocationFound: (Location) -> Unit) {
    val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    fusedClient.lastLocation.addOnSuccessListener { location: Location? ->
        location?.let { onLocationFound(it) }
    }
}