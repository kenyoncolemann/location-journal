package com.example.location_journal.ui.theme

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.location_journal.data.JournalEntryItem
import com.example.location_journal.data.UserEntryItem
import com.example.location_journal.viewmodel.JournalViewModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import android.location.Geocoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@SuppressLint("MissingPermission")
@Composable
fun JournalEntryScreen(viewModel: JournalViewModel, user: UserEntryItem) {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Fetching location...") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        getLocation(context) { loc ->
            coroutineScope.launch {
                val name = withContext(Dispatchers.IO) {
                    getLocationName(context, loc.latitude, loc.longitude)
                }
                location = name
            }
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
                    if (location == "Fetching location...") {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Still fetching location. Please wait...")
                        }
                        return@Button
                    }
                    val currentDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date())
                    val entry = JournalEntryItem(
                        userId = user.id,
                        date = currentDate,
                        happy = 0.0,
                        sad = 0.0,
                        angry = 0.0,
                        surprised = 0.0,
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
        if (location != null) {
            Log.d("LocationDebug", "Got location: ${location.latitude}, ${location.longitude}")
            onLocationFound(location)
        } else {
            Log.w("LocationDebug", "lastLocation is null")

            // Fallback: try to actively request location
            fusedClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { newLoc ->
                if (newLoc != null) {
                    Log.d("LocationDebug", "Fetched new location: ${newLoc.latitude}, ${newLoc.longitude}")
                    onLocationFound(newLoc)
                } else {
                    Log.e("LocationDebug", "Still couldn't get location")
                }
            }.addOnFailureListener {
                Log.e("LocationDebug", "Failed to get current location", it)
            }
        }
    }.addOnFailureListener {
        Log.e("LocationDebug", "lastLocation failed", it)
    }
}


fun getLocationName(context: Context, lat: Double, lon: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(lat, lon, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            listOfNotNull(address.locality, address.adminArea).joinToString(", ")
        } else {
            "Unknown location"
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "Unknown location"
    }
}