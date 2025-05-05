package com.example.location_journal.viewmodel



import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.location_journal.analysis.SentimentAnalyzer
import com.example.location_journal.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JournalViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = JournalDatabase.getDatabase(application).journalDao()

    val entries: StateFlow<List<JournalEntryItem>> = dao.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEntry(entry: JournalEntryItem) {
        viewModelScope.launch {
            val mood = SentimentAnalyzer.analyze(entry.text)
            val entity = JournalEntryItem(
                date = entry.date,
                mood = mood,
                text = entry.text,
                location = entry.location
            )
            dao.insertEntry(entity)
        }
    }

    fun deleteEntry(entry: JournalEntryItem) {
        viewModelScope.launch {
            dao.deleteEntry(entry)
        }
    }
}