package no.hiof.danieljr.drawerly.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import no.hiof.danieljr.drawerly.R
import no.hiof.danieljr.drawerly.ui.login.LoginState
import no.hiof.danieljr.drawerly.ui.login.LoginViewModel

@Composable
fun LoginOrCreateAccountScreen(
    loginViewModel: LoginViewModel,
    onGoogleLoginClick: () -> Unit,
    onGoogleCreateClick: () -> Unit
) {
    val loginState by loginViewModel.loginState.collectAsState()
    

    val LavenderPurple = Color(0xFFB39DDB)
    val DarkLavenderPurple = Color(0xFF7E57C2)
    val DarkerLavenderPurple = Color(0xFF5E35B1)
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val boxHeight = if (screenHeight < 700.dp) {
        screenHeight * 0.85f
    } else {
        screenHeight * 0.75f
    }

    Box(modifier = Modifier.fillMaxSize()) {

        ResponsiveTopImage(R.drawable.drawerly)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(boxHeight)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 42.dp, topEnd = 42.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(LavenderPurple, DarkLavenderPurple, DarkerLavenderPurple)
                    )
                )
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                item { Spacer(modifier = Modifier.width(6.dp)) }
                item {
                    GlassCard(
                        title = "Login",
                        subtitle = "or sign in with",
                        swipeText = "Swipe to create account",
                        email = loginViewModel.email,
                        onEmailChange = loginViewModel::onEmailChange,
                        password = loginViewModel.password,
                        onPasswordChange = loginViewModel::onPasswordChange,
                        onMainButtonClick = loginViewModel::onLoginClick,
                        onGoogleClick = onGoogleLoginClick,
                        btnText = "Login"
                    )

                }
                item {
                    GlassCard(
                        title = "Add Account",
                        subtitle = "or create account with",
                        swipeText = "",
                        email = loginViewModel.email,
                        onEmailChange = loginViewModel::onEmailChange,
                        password = loginViewModel.password,
                        onPasswordChange = loginViewModel::onPasswordChange,
                        name = loginViewModel.name,
                        onNameChange = loginViewModel::onNameChange,
                        onMainButtonClick = loginViewModel::onCreateAccountClick,
                        onGoogleClick = onGoogleCreateClick,
                        btnText = "Add Account"
                    )

                }
                item { Spacer(modifier = Modifier.width(6.dp)) }
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    when (loginState) {
        is LoginState.Loading -> Text("Logger inn...")
        is LoginState.Success -> Text("Innlogging vellykket!")
        is LoginState.Error -> Text("Feil: ${(loginState as LoginState.Error).error}")
        else -> {}
    }
}

@Composable
fun ResponsiveTopImage(resourceId: Int) {
    BoxWithConstraints {
        val imageHeight = if (maxHeight < 700.dp) maxHeight * 0.15f else maxHeight * 0.3f
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            Image(
                painter = painterResource(resourceId),
                contentDescription = "Top background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun GlassCard(
    title: String,
    subtitle: String,
    swipeText: String,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    name: String = "",
    onNameChange: (String) -> Unit = {},
    onMainButtonClick: () -> Unit,
    onGoogleClick: () -> Unit,
    btnText : String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.40f),
                        Color.White.copy(alpha = 0.10f)
                    )
                )
            )
            .blur(16.dp)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(10.dp))
            InputField(label = "Email", value = email, onValueChange = onEmailChange)
            Spacer(modifier = Modifier.height(10.dp))
            InputField(label = "Password", value = password, onValueChange = onPasswordChange)
            Spacer(modifier = Modifier.height(10.dp))
            if (title.contains("Add", ignoreCase = true)) {
                InputField(label = "Username", value = name, onValueChange = onNameChange)
                Spacer(modifier = Modifier.height(16.dp))
            }
            Button(onClick = onMainButtonClick

                , modifier = Modifier.fillMaxWidth()) {
                Text(btnText)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = subtitle, color = Color.White, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(58.dp)
                    .clickable { onGoogleClick() }
            )
            Spacer(modifier = Modifier.height(10.dp))
            if (swipeText.isNotEmpty()) {
                Row {
                    Text(text = swipeText, color = Color.White, style = MaterialTheme.typography.bodySmall)
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Arrow",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        textStyle = TextStyle(color = Color.White),
       // colors = TextFieldDefaults.outlinedTextFieldColors(
       //     containerColor = Color.Transparent,
       //     focusedBorderColor = Color.White.copy(alpha = 0.5f),
       //     unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
       //     cursorColor = Color.White
       // )
    )
}

