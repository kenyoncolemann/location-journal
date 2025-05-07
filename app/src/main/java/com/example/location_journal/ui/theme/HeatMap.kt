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
            val entryList = viewModel.getEntriesForUser(user.id).first()

            val emotionData = EmotionData(
                data = mapOf(
                    "Happy" to entryList.map { it.happy },
                    "Sad" to entryList.map { it.sad },
                    "Angry" to entryList.map { it.angry },
                    "Surprised" to entryList.map { it.surprised }
                ),
                index = entryList.mapIndexed { i, _ -> "Journal ${i + 1}" }
            )

            val json = Json.encodeToString(emotionData)
            val resultBitmap = requestHeatmapFromServer(json)

            bitmap = resultBitmap
        } catch (e: Exception) {
            error = "Failed to load heatmap: ${e.localizedMessage}"
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            bitmap != null -> {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Heatmap",
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                )
            }
            error != null -> {
                Text(text = error ?: "Unknown error", color = MaterialTheme.colorScheme.error)
            }
            else -> {
                CircularProgressIndicator()
            }
        }
    }
}
