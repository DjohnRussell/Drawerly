package no.hiof.danieljr.drawerly.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import no.hiof.danieljr.drawerly.R

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    
    // Animation state
    var startAnimation by remember { mutableStateOf(false) }
    
    val targetHeightFactor = if (screenHeight < 700.dp) 0.15f else 0.3f
    val targetHeight = screenHeight * targetHeightFactor
    
    // Animate height and position
    val heightAnimation by animateDpAsState(
        targetValue = if (startAnimation) targetHeight else screenHeight,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "height"
    )

    val LavenderPurple = Color(0xFFB39DDB)
    val DarkerLavenderPurple = Color(0xFF5E35B1)

    LaunchedEffect(key1 = true) {
        delay(500) // Vent litt før start
        startAnimation = true
        delay(1000) // Vent på animasjon
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(LavenderPurple, DarkerLavenderPurple)
                )
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.drawerly),
            contentDescription = "Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(heightAnimation)
                .align(Alignment.TopCenter)
        )
    }
}
