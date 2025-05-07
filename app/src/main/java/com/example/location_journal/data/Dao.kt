package com.example.location_journal.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// dao for journal entries
@Dao
interface JournalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntryItem) // add or replace

    @Query("SELECT * FROM journal_entries")
    fun getAllEntries(): Flow<List<JournalEntryItem>> // gets all entries

    @Delete
    suspend fun deleteEntry(entry: JournalEntryItem) // delete entry

    @Query("SELECT * FROM journal_entries WHERE userId = :userId")
    fun getEntriesForUser(userId: Int): Flow<List<JournalEntryItem>> // get entries for a user
}

// dao for users
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: UserEntryItem) // add or replace

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    fun login(username: String, password: String): UserEntryItem? // login

    @Query("SELECT * FROM users")
    fun getAllEntries(): Flow<List<UserEntryItem>> // get all users

    @Delete
    suspend fun deleteEntry(entry: UserEntryItem) // delete user
}