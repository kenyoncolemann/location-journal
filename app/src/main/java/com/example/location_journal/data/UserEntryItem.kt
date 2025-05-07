package com.example.location_journal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// data class for users
@Entity(tableName = "users")
data class UserEntryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String
)