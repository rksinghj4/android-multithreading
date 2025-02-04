package com.multithreading.asynctask

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.multithreading.LONG_RUNNING_TASK_TIMEOUT
import com.multithreading.ui.theme.MultiThreadsLearningTheme

class TaskOnBGThreadUsingAsyncTask : ComponentActivity() {
    private val viewModel by viewModels<AsyncTaskViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val threadIdFromPreviousScreen = intent.extras?.getLong(THREAD_ID)

        setContent {
            MultiThreadsLearningTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        var counterState by rememberSaveable {
                            mutableIntStateOf(0)
                        }

                        val progressSate by viewModel.progressStateFlow.collectAsState()
                        val resultState by viewModel.resultStateFlow.collectAsState()

                        Text(text = "Previous screen: ThreadId:  $threadIdFromPreviousScreen")

                        Text(text = "Current Thread Id: ${Thread.currentThread().id}")

                        Text(text = "Current Thread name: ${Thread.currentThread().name}")

                        LinearProgressIndicator(
                            progress = progressSate.toFloat()/ LONG_RUNNING_TASK_TIMEOUT
                        )
                        Text(
                            text = "Result : $resultState"
                        )

                        Button(onClick = { viewModel.stopLongRunningTask() }) {
                            Text(text = "Stop long running  task")
                        }
                    }

                }
            }
        }
    }

    companion object {
        private const val THREAD_ID = "thread_id"
        fun show(context: Context, threadId: Long) {
            Intent(context, TaskOnBGThreadUsingAsyncTask::class.java).apply {
                putExtra(THREAD_ID, threadId)
            }.also {
                context.startActivity(it)
            }
        }
    }
}