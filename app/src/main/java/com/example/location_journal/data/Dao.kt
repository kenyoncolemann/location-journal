package com.example.location_journal.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntryItem)

    @Query("SELECT * FROM journal_entries")
    fun getAllEntries(): Flow<List<JournalEntryItem>>

    @Delete
    suspend fun deleteEntry(entry: JournalEntryItem)
}