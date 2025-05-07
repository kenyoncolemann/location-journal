package com.example.location_journal.viewmodel



import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.location_journal.analysis.SentimentAnalyzer
import com.example.location_journal.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import requestAbsaMoodList

// view model for journal entries
class JournalViewModel(application: Application) : AndroidViewModel(application) {

    // dao for journal entries
    private val dao = JournalDatabase.getDatabase(application).journalDao()

    // list of all entries
    val entries: StateFlow<List<JournalEntryItem>> = dao.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // get entries for a user
    fun getEntriesForUser(userId: Int): Flow<List<JournalEntryItem>> {
        return dao.getEntriesForUser(userId)
    }

    // add or replace an entry
    fun addEntry(entry: JournalEntryItem) {
        viewModelScope.launch {
            try {
                val mood = requestAbsaMoodList(entry.text)
                val entity = JournalEntryItem(
                    userId = entry.userId,
                    date = entry.date,
                    happy = mood[0],
                    sad = mood[1],
                    angry = mood[2],
                    surprised = mood[3],
                    text = entry.text,
                    location = entry.location
                )
                dao.insertEntry(entity)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // delete an entry
    fun deleteEntry(entry: JournalEntryItem) {
        viewModelScope.launch {
            dao.deleteEntry(entry)
        }
    }
}