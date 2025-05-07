package com.example.location_journal.ui.theme

import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.location_journal.data.UserDao
import com.example.location_journal.data.UserEntryItem

// login screen ui and logic
@Composable
fun LoginScreen(
    userDao: UserDao,
    onLogin: (UserEntryItem) -> Unit, // callback for successful login
    onNavigateToRegister: () -> Unit // callback to registration
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(Modifier.padding(24.dp)) {
        Text("Login", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        // inputs
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Spacer(Modifier.height(16.dp))

        // login button
        Button(onClick = {
            scope.launch {
                val user = withContext(Dispatchers.IO) {
                    userDao.login(username, password)
                }

                if (user != null) {
                    onLogin(user)
                } else {
                    error = "Invalid credentials"
                }
            }
        }) {
            Text("Log In")
        }

        // button to go to registration screen
        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register")
        }

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}
