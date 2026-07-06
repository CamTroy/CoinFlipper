/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package dev.spacebanana.coinflipper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonColors
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.CircularProgressIndicatorDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ProgressIndicatorDefaults
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.ShapeDefaults
import androidx.wear.compose.material3.Shapes
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import dev.spacebanana.coinflipper.R
import dev.spacebanana.coinflipper.presentation.theme.CoinFlipperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearApp("Android")
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    val context = LocalContext.current
    var isFlipping: Boolean by remember { mutableStateOf(false) }
    val flippingTexts = remember {
        try {
            context.assets.open("flippingtext.csv").bufferedReader().use { it.readText() }.split(",")
        } catch (e: Exception) {
            listOf("Flipping...")
        }
    }
    var flipText: String by remember { mutableStateOf(flippingTexts.random()) }
    CoinFlipperTheme {
        AppScaffold {
            val listState = rememberTransformingLazyColumnState()
            val transformationSpec = rememberTransformationSpec()
            ScreenScaffold(
                scrollState = listState,
            ) { contentPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (!isFlipping) {
                        Button(
                            modifier = Modifier.size(100.dp),
                            onClick = {
                                flipText = flippingTexts.random()
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
                    } else {
                        val currentProgress = 0f
                        var progress by remember { mutableFloatStateOf(currentProgress) }
                        val animatedProgress = animateFloatAsState(
                            targetValue = progress,
                            label = "flipProgress"
                        ).value

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
                                progress = { animatedProgress },
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
    WearApp("Preview Android")
}