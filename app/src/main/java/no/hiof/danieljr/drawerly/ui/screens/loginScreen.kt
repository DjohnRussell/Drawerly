package no.hiof.danieljr.drawerly.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(loginViewModel: LoginViewModel) {

    val loginState by loginViewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("E-post") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Passord") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { loginViewModel.login(email, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logg inn")
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (loginState) {
            is LoginState.Loading -> Text("Logger inn...")
            is LoginState.Success -> Text("Innlogging vellykket!")
            is LoginState.Error -> Text("Feil: ${(loginState as LoginState.Error).error}")
            else -> { }
        }
    }
}
