package com.example.location_journal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// data class for journal entries
@Entity(tableName = "journal_entries")
data class JournalEntryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val date: String,
    val happy: Double,
    val sad: Double,
    val angry: Double,
    val surprised: Double,
    val text: String,
    val location: String
)