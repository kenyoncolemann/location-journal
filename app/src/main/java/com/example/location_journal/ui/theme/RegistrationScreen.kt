package com.example.location_journal.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.location_journal.data.UserDao
import com.example.location_journal.data.UserEntryItem
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    userDao: UserDao,
    onRegistered: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(Modifier.padding(24.dp)) {
        Text("Register", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Spacer(Modifier.height(16.dp))

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
