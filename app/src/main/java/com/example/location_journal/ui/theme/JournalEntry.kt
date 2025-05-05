package com.example.location_journal.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JournalEntryScreen() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Journal Entry", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Write your thoughts:") },
            modifier = Modifier.fillMaxWidth().height(150.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { /*  save in database */ }) {
            Text(text = "Save Entry")
        }
    }
}