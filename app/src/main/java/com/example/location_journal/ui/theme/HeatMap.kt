package com.example.location_journal.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.location_journal.data.UserEntryItem
import com.example.location_journal.viewmodel.JournalViewModel

@Composable
fun HeatMapScreen(viewModel: JournalViewModel, user: UserEntryItem) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Heat Map", style = MaterialTheme.typography.headlineMedium)
    }
}