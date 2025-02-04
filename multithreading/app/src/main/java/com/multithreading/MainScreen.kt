package com.multithreading

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.multithreading.ui.theme.MultiThreadsLearningTheme

data class Actions(
    val onRunLoopOnMainThread: () -> Unit = {},
    val onRunLoopSeparateThreadThread: () -> Unit = {}
)

@Composable
fun MainScreen(action: Actions) {
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Run Loop on MainThread")
        }

        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "TextButton: Run Loop on Separate Thread")
        }

        ElevatedButton(onClick = { /*TODO*/ }) {
            Text(text = "ElevatedButton: Run Loop on Separate")
        }

        ExtendedFloatingActionButton(
            text = {
                Text(text = "Test ExtendedFloatingActionButton")
            },
            icon = {
                Icon(
                    painter = painterResource(id = androidx.core.R.drawable.ic_call_answer_video),
                    contentDescription = "testExtendedFloatingActionButton"
                )
            },
            onClick = {

            }
        )
    }
}

/**
 * For @PreviewFontScale and @PreviewScreenSizes we need to add
 * implementation("androidx.compose.ui:ui-tooling-preview-android:1.7.7")
 *
 */
@PreviewFontScale
@PreviewScreenSizes
@Preview(
    showBackground = true,
    showSystemUi = true,
    apiLevel = 34,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    fontScale = 1f,
    device = Devices.NEXUS_5
)

@Composable
fun MainPreview() {
    MultiThreadsLearningTheme {
        MainScreen(action = Actions())
    }
}