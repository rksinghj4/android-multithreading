package com.multithreading.customlooper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.multithreading.LONG_RUNNING_TASK_TIMEOUT
import com.multithreading.R
import com.multithreading.ui.theme.MultiThreadsLearningTheme

class CustomLooperActivity : ComponentActivity() {
    private val viewModel by viewModels<CustomLooperViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultiThreadsLearningTheme {
                Surface {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        var counterState by rememberSaveable {
                            mutableIntStateOf(0)
                        }

                        val progressSate by viewModel.progressStateFlow.collectAsState()
                        val resultState by viewModel.resultStateFlow.collectAsState()
                        Text(text = "Current Thread Id: ${Thread.currentThread().id}")
                        val customTextView = AndroidView(factory = {
                            TextView(it)
                        }, update = {
                            it.text = getString(R.string.update, resultState)
                        })
                        Button(onClick = {
                            /**
                             * Posting the task to same looper,
                             * it will add the runnable to same message queue
                             */
                            viewModel.customLooper.mHandler?.post(
                                Runnable {
                                    runOnUiThread(
                                        Runnable {
                                            viewModel.update()
                                            //here we can update any view
                                        }
                                    )
                                }
                            )
                        }) {
                            Text(text = "Update UI using Custom Looper")
                        }

                        Button(onClick = { viewModel.communicationBetweenThreadToCustomLooper() }) {
                            Text(text = "Communication b/w Thread and Custom Looper")
                        }


                        LinearProgressIndicator(
                            progress = progressSate.toFloat() / LONG_RUNNING_TASK_TIMEOUT
                        )
                        Text(
                            text = "Result : $resultState"
                        )

                        Button(onClick = { viewModel.stop() }) {
                            Text(text = "Stop long running  task")
                        }
                    }

                }
            }
        }
    }

    companion object {
        fun show(context: Context) {
            Intent(context, CustomLooperActivity::class.java).also {
                context.startActivity(it)
            }
        }
    }
}