package no.hiof.danieljr.drawerly.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import no.hiof.danieljr.drawerly.ui.login.LoginState
import no.hiof.danieljr.drawerly.ui.login.LoginViewModel
import no.hiof.danieljr.drawerly.ui.screens.LoginOrCreateAccountScreen
import no.hiof.danieljr.drawerly.ui.screens.SplashScreen
import no.hiof.danieljr.drawerly.ui.screens.HomeScreen

@Composable
fun LaunchApp() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState by loginViewModel.loginState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Naviger til Home når innlogging er vellykket
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // Funksjon for å starte Google Sign-In
    val onGoogleSignIn = {
        coroutineScope.launch {
            val credentialManager = CredentialManager.create(context)
            
            // VIKTIG: Bytt ut denne med din Web Client ID fra Firebase Console -> Authentication -> Sign-in method -> Google
            val serverClientId = "YOUR_SERVER_CLIENT_ID.apps.googleusercontent.com"
            
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(serverClientId)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential
                
                if (credential is GoogleIdTokenCredential) {
                    loginViewModel.signInWithGoogle(credential.idToken)
                }
            } catch (e: Exception) {
                // Håndter feil (f.eks. avbrutt av bruker)
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(onAnimationFinished = {
                if (loginViewModel.getCurrentUser() != null) {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            })
        }
        
        composable(Screen.Login.route) {
            LoginOrCreateAccountScreen(
                loginViewModel = loginViewModel,
                onGoogleLoginClick = { onGoogleSignIn() },
                onGoogleCreateClick = { onGoogleSignIn() }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(onLogout = {
                loginViewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            })
        }
    }
}
