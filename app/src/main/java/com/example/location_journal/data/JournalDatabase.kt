package com.example.location_journal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// room database for journal entries
@Database(entities = [JournalEntryItem::class], version = 3, exportSchema = false)
abstract class JournalDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao // allows access to dao

    companion object {
        @Volatile
        private var INSTANCE: JournalDatabase? = null

        // returns database instance
        fun getDatabase(context: Context): JournalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JournalDatabase::class.java,
                    "journal_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
