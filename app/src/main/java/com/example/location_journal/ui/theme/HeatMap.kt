package com.example.location_journal.ui.theme

import android.graphics.Bitmap
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.location_journal.data.UserEntryItem
import com.example.location_journal.viewmodel.JournalViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import requestHeatmapFromServer
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.first
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

// Create a class to store the emotion data
@Serializable
data class EmotionData(
    val data: Map<String, List<Double>>,
    val index: List<String>
)

@Composable
fun HeatMapScreen(viewModel: JournalViewModel, user: UserEntryItem) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            // Get all entries
            val entryList = viewModel.getEntriesForUser(user.id).first()

            // Get all emotions
            val emotionData = EmotionData(
                data = mapOf(
                    "Happy" to entryList.map { it.happy },
                    "Sad" to entryList.map { it.sad },
                    "Angry" to entryList.map { it.angry },
                    "Surprised" to entryList.map { it.surprised }
                ),
                index = entryList.mapIndexed { i, _ -> "Journal ${i + 1}" }
            )

            // Create the json and send to Client to send to server
            val json = Json.encodeToString(emotionData)
            val resultBitmap = requestHeatmapFromServer(json)

            // Heatmap
            bitmap = resultBitmap
        } catch (e: Exception) {
            error = "No entries to display"
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {

            // Display the image on the screen
            bitmap != null -> {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Heatmap",
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                )
            }
            // Error if the emotions have mismatched amounts of points
            error != null -> {
                Text(text = error ?: "Unknown error", color = MaterialTheme.colorScheme.error)
            }
            else -> {
                CircularProgressIndicator()
            }
        }
    }
}
