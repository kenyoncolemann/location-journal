package com.example.location_journal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.location_journal.viewmodel.JournalViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.location_journal.data.JournalEntryItem


@Composable
fun ProfileScreen(viewModel: JournalViewModel) {
    val entries by viewModel.entries.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Username", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Your saved journal entries:", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(entries) { entry ->
                JournalEntryCard(
                    entry = entry,
                    onDelete = { viewModel.deleteEntry(entry) }
                )
            }
        }
    }
}

@Composable
fun JournalEntryCard(entry: JournalEntryItem, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = entry.date, style = MaterialTheme.typography.labelMedium)
                Text(
                    text = entry.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = entry.mood,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = entry.text, style = MaterialTheme.typography.bodyLarge)
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Entry",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}