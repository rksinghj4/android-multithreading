package com.multithreading

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.multithreading.asynctask.TaskOnBGThreadUsingAsyncTask
import com.multithreading.customlooper.CustomLooperActivity
import com.multithreading.threading.TaskOnMainThread
import com.multithreading.threading.TaskOnSeparateThread
import com.multithreading.ui.theme.MultiThreadsLearningTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultiThreadsLearningTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val context = LocalContext.current
                    Column {
                        Text(text = "Executed on : Thread ${Thread.currentThread().name}, Id: ${Thread.currentThread().id}")
                        MainScreen(clickAction(context))
                    }
                }
            }
        }
    }

    private fun clickAction(context: Context) = Actions(
        onRunLoopOnMainThread = {
            TaskOnMainThread.show(context = context, Thread.currentThread().id)
        },
        onRunLoopOnSeparateThread = {
            TaskOnSeparateThread.show(context = context, Thread.currentThread().id)
        },
        onRunLoopInAsyncTask = {
            TaskOnBGThreadUsingAsyncTask.show(context, Thread.currentThread().id)
        },
        onRunCustomLooper = {
            CustomLooperActivity.show(context)
        }
    )
}

