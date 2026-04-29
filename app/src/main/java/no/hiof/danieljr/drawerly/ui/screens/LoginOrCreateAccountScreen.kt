package no.hiof.danieljr.drawerly.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.hiof.danieljr.drawerly.R
import no.hiof.danieljr.drawerly.ui.components.*
import no.hiof.danieljr.drawerly.ui.login.LoginState
import no.hiof.danieljr.drawerly.ui.login.LoginViewModel

@Composable
fun LoginOrCreateAccountScreen(
    loginViewModel: LoginViewModel,
    onGoogleLoginClick: () -> Unit,
    onGoogleCreateClick: () -> Unit
) {
    val loginState by loginViewModel.loginState.collectAsState()
    val isLoading = loginState is LoginState.Loading

    if (isLoading) {
        ShimmerScreen()
    } else {
        val LavenderPurple = Color(0xFFB39DDB)
        val DarkLavenderPurple = Color(0xFF7E57C2)
        val DarkerLavenderPurple = Color(0xFF5E35B1)
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp

        val boxHeight = if (screenHeight < 700.dp) {
            screenHeight * 0.85f
        } else {
            screenHeight * 0.75f
        }

        val pagerState = rememberPagerState(pageCount = { 2 })

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
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth(),
                        pageSpacing = 16.dp,
                        verticalAlignment = Alignment.CenterVertically
                    ) { page ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (page == 0) {
                                LoginGlassCard(
                                    title = "Login",
                                    subtitle = "or sign in with",
                                    swipeText = "Swipe for Register",
                                    email = loginViewModel.email,
                                    onEmailChange = loginViewModel::onEmailChange,
                                    password = loginViewModel.password,
                                    onPasswordChange = loginViewModel::onPasswordChange,
                                    onMainButtonClick = loginViewModel::onLoginClick,
                                    onGoogleClick = onGoogleLoginClick,
                                    btnText = "Login"
                                )
                            } else {
                                LoginGlassCard(
                                    title = "Add Account",
                                    subtitle = "or create account with",
                                    swipeText = "Swipe for Login",
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
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Page indicator
                    Row(
                        Modifier
                            .height(20.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(2) { iteration ->
                            val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color)
                                    .size(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Show error messages if any
    if (loginState is LoginState.Error) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Snackbar(
                modifier = Modifier.padding(16.dp).padding(bottom = 32.dp),
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ) {
                Text((loginState as LoginState.Error).error)
            }
        }
    }
}

@Composable
fun LoginGlassCard(
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
    btnText: String
) {
    GlassCard(
        modifier = Modifier.width(300.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall
        )
        SpaceEm(20)
        InputField(label = "Email", value = email, onValueChange = onEmailChange)
        SpaceEm(12)
        InputField(label = "Password", value = password, onValueChange = onPasswordChange)
        
        if (title.contains("Add", ignoreCase = true)) {
            SpaceEm(12)
            InputField(label = "Username", value = name, onValueChange = onNameChange)
        }
        
        SpaceEm(24)
        
        // Bruker den nye PrimaryButton her
        PrimaryButton(
            text = btnText,
            onClick = onMainButtonClick
        )
        
        SpaceEm(24)
        Text(
            text = subtitle,
            color = Color.White.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodyMedium
        )
        SpaceEm(12)
        IconButton(
            onClick = onGoogleClick,
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google",
                tint = Color.Unspecified
            )
        }
        
        if (swipeText.isNotEmpty()) {
            SpaceEm(16)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = swipeText,
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )
                Icon(
                    if (title == "Login") Icons.AutoMirrored.Filled.KeyboardArrowRight else Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
