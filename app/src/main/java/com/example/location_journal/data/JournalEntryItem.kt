package com.example.location_journal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val mood: String,
    val text: String,
    val location: String
)