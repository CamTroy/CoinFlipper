/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package dev.spacebanana.coinflipper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.CircularProgressIndicatorDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ProgressIndicatorDefaults
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.ShapeDefaults
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import dev.spacebanana.coinflipper.presentation.theme.CoinFlipperTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    val context = LocalContext.current
    var isFlipping: Boolean by remember { mutableStateOf(false) }
    var isFlipped: Boolean by remember { mutableStateOf(false) }
    val flippingTexts = remember {
        try {
            context.assets.open("flippingtext.csv").bufferedReader().use { it.readText() }
                .split(",")
        } catch (e: Exception) {
            listOf("Flipping...")
        }
    }
    var flipText: String by remember { mutableStateOf(flippingTexts.random()) }
    CoinFlipperTheme {
        AppScaffold {
            val listState = rememberTransformingLazyColumnState()
            ScreenScaffold(
                scrollState = listState,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (!isFlipping && !isFlipped) {
                        Button(
                            modifier = Modifier.size(100.dp),
                            onClick = {
                                isFlipping = true
                            },
                            colors = ButtonDefaults.buttonColors(),
                            content = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        text = "Flip",
                                        fontSize = MaterialTheme.typography.labelLarge.fontSize,
                                        fontFamily = FontFamily.Monospace,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            },
                            shape = ShapeDefaults.ExtraLarge,
                        )
                    } else if (isFlipped && !isFlipping) {

                        val result = (0..1).random()
                        var resultText by remember { mutableStateOf("") }

                        LaunchedEffect(isFlipped) {
                            if (isFlipped) {
                                when (result) {
                                    0 -> {
                                        resultText = "Heads"
                                    }

                                    1 -> {
                                        resultText = "Tails"
                                    }
                                }
                                delay(5000.milliseconds)
                                isFlipped = false
                            }
                        }

                        Text(text = resultText, style = MaterialTheme.typography.bodyLarge)
                    } else {
                        var progress by remember { mutableFloatStateOf(0f) }

                        LaunchedEffect(isFlipping) {
                            if (isFlipping) {
                                for (i in 0..100) {
                                    if (i % 20 == 0) {
                                        flipText = flippingTexts.random()
                                    }
                                    progress = i / 100f
                                    delay(30.milliseconds)
                                }
                                delay(500.milliseconds)
                                isFlipping = false
                                isFlipped = true
                            }
                        }

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.fillMaxSize(),
                                colors = ProgressIndicatorDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = .3f),
                                ),
                                progress = { progress },
                                strokeWidth = CircularProgressIndicatorDefaults.largeStrokeWidth,
                            )
                            Text(
                                text = flipText,
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
    WearApp()
}
