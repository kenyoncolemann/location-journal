package com.example.location_journal.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.location_journal.data.UserDao
import com.example.location_journal.data.UserEntryItem
import kotlinx.coroutines.launch

// registration screen ui and logic
@Composable
fun RegistrationScreen(
    userDao: UserDao,
    onRegistered: () -> Unit // callback for successful registration
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // layout for the form
    Column(Modifier.padding(24.dp)) {
        Text("Register", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        // inputs
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Spacer(Modifier.height(16.dp))

        // register button, adds to db
        Button(onClick = {
            scope.launch {
                val newUser = UserEntryItem(username = username, password = password)
                userDao.insertEntry(newUser) // ✅ Now safe inside coroutine
                onRegistered()
            }
        }) {
            Text("Register")
        }
    }
}
