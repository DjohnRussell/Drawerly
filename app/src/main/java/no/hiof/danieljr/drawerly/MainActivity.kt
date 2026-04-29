package no.hiof.danieljr.drawerly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import no.hiof.danieljr.drawerly.navigation.LaunchApp
import no.hiof.danieljr.drawerly.ui.theme.DrawerlyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrawerlyTheme {
                LaunchApp()
            }
        }
    }
}
