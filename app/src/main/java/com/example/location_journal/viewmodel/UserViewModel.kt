package com.example.location_journal.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.location_journal.data.UserEntryItem

// view model for users
class UserViewModel(application: Application) : AndroidViewModel(application) {
    var currentUser: UserEntryItem? = null
}