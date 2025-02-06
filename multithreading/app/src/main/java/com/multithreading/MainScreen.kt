package com.multithreading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.multithreading.ui.theme.MultiThreadsLearningTheme

data class Actions(
    val onRunLoopOnMainThread: () -> Unit = {},
    val onRunLoopOnSeparateThread: () -> Unit = {},
    val onRunLoopInAsyncTask: () -> Unit = {},
    val onRunCustomLooper: () -> Unit = {},
)

@Composable
fun MainScreen(clickAction: Actions) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { clickAction.onRunLoopOnMainThread.invoke() }) {
            Text(text = "Run Loop on MainThread")
        }
        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color.Black,
                    shape = DottedShape(step = 20.dp)
                )
        )
        TextButton(modifier = Modifier.background(Color.Green),
            onClick = { clickAction.onRunLoopOnSeparateThread() }) {
            Text(text = "TextButton: Run Loop on Separate Thread")
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color.Green,
                    shape = DottedShape(step = 16.dp)
                )
        )

        ElevatedButton(onClick = clickAction.onRunLoopInAsyncTask) {
            Text(text = "ElevatedButton: Run Loop in AsyncTask")
        }

        Spacer(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color.DarkGray,
                    shape = DottedShape(step = 8.dp)
                )
        )

        ExtendedFloatingActionButton(
            text = {
                Text(text = "CustomLooper communication")
            },
            icon = {
                Icon(
                    painter = painterResource(id = androidx.core.R.drawable.ic_call_answer_video),
                    contentDescription = "testExtendedFloatingActionButton"
                )
            },
            onClick = {
                clickAction.onRunCustomLooper.invoke()
            }
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    color = Color.Blue,
                    shape = DottedShape(step = 4.dp)
                )
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
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
    device = Devices.NEXUS_5
)
@Composable
fun MainPreview() {
    MultiThreadsLearningTheme {
        MainScreen(clickAction = Actions())
    }
}