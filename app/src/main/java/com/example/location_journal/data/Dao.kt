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

    @Query("SELECT * FROM journal_entries WHERE userId = :userId")
    fun getEntriesForUser(userId: Int): Flow<List<JournalEntryItem>>
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: UserEntryItem)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    fun login(username: String, password: String): UserEntryItem?

    @Query("SELECT * FROM users")
    fun getAllEntries(): Flow<List<UserEntryItem>>

    @Delete
    suspend fun deleteEntry(entry: UserEntryItem)
}