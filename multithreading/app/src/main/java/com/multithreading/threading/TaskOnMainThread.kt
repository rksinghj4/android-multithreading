package com.multithreading.threading

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multithreading.TAG
import com.multithreading.ui.theme.MultiThreadsLearningTheme
import java.util.concurrent.atomic.AtomicBoolean

class TaskOnMainThread : ComponentActivity() {
    private var shouldInfiniteLoop = AtomicBoolean(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val threadIdFromPreviousScreen = intent.extras?.getLong(THREAD_ID)
        setContent {
            MultiThreadsLearningTheme {
                Surface {
                    Scaffold(modifier = Modifier.padding(16.dp)) { paddingValues ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            var counterState by rememberSaveable {
                                mutableIntStateOf(0)
                            }
                            Text(text = "Previous screen ThreadId: $threadIdFromPreviousScreen")

                            Text(text = "Thread Id: ${Thread.currentThread().id}")

                            Text(text = "name: ${Thread.currentThread().name}")

                            Text(text = "threadGroup: ${Thread.currentThread().threadGroup}")

                            Text(text = "name: ${Thread.currentThread().name} and Counter: $counterState")



                            Row {
                                Button(onClick = {
                                    shouldInfiniteLoop.set(true)
                                    /**
                                     * This infinite loop will occupy the main thread.
                                     * Not other UI rendering, animation, and click will be accepted until
                                     * loop terminates. Once main looper is free to take click events/ui updates
                                     * then it will deque them and show the results.
                                     */
                                    while (Thread.currentThread().isAlive && shouldInfiniteLoop.get()) {
                                        Log.d(TAG, "Counter: $counterState")
                                        counterState += 1
                                        Thread.sleep(1000)
                                        if (counterState > 10) {
                                            /**
                                             * Modern ways to suspend/stop a thread are by using a boolean flag and Thread.interrupt() method.
                                             *
                                             */
                                            Thread.currentThread().interrupt()//This will end the running process.
                                        }
                                        if (counterState > 20) {
                                            break
                                        }
                                    }
                                }) {
                                    Text(text = "Start loop")
                                }

                                Button(onClick = {
                                    //This click itself/lambda will not be processed/executed by
                                    // main looper because of infinite loop above. So Thread.sleep(2000)
                                    // will not executed until main thread is free.
                                    counterState += 1
                                    Log.d(TAG, "Stop loop clicked $counterState")
                                    //Thread.currentThread().join(5000)
                                    Thread.sleep(1000)
                                    shouldInfiniteLoop.set(false)
                                }) {
                                    Text(text = "Stop loop")
                                }
                            }


                            //Text(text = "contextClassLoader: ${Thread.currentThread().contextClassLoader}")
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val THREAD_ID = "thread_id"
        fun show(context: Context, threadId: Long) {
            Intent(context, TaskOnMainThread::class.java).apply {
                putExtra(THREAD_ID, threadId)
            }.also {
                context.startActivity(it)
            }
        }
    }
}