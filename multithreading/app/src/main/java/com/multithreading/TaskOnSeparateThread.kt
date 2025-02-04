package com.multithreading

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
import com.multithreading.ui.theme.MultiThreadsLearningTheme
import java.util.concurrent.atomic.AtomicBoolean

class TaskOnSeparateThread : ComponentActivity() {
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
                            Text(text = "Previous screen: name - ${Thread.currentThread().name}, ThreadId - $threadIdFromPreviousScreen")

                            Text(text = "Thread Id: ${Thread.currentThread().id}")

                            Text(text = "Thread name: ${Thread.currentThread().name}")

                            Text(text = "threadGroup: ${Thread.currentThread().threadGroup}")

                            Text(text = "Thread name: ${Thread.currentThread().name}, Id: ${Thread.currentThread().id} and Counter: $counterState")

                            /**
                             * Background thread can't update the view.
                             * Only thread which created the view hierarchy can update the Views(i.e.Main)
                             */

                            Row {
                                Button(onClick = {
                                    shouldInfiniteLoop.set(true)
                                    /**
                                     * Since it's separate thread
                                     * This infinite loop will not occupy the main thread.
                                     * All other UI rendering, animation, and click will be accepted by main looper.
                                     */
                                    Thread(Runnable {
                                        while (Thread.currentThread().isAlive && shouldInfiniteLoop.get()) {
                                            Log.d(
                                                TAG,
                                                "In While:  Thread name -  ${Thread.currentThread().name}, Id - ${Thread.currentThread().id}, Counter - $counterState"
                                            )
                                            /**
                                             * Advantage of compose state: To update UI from bg thread
                                             * here we are not creating -
                                             *
                                             * Handler(Looper.getMainLooper())
                                             * .post(Runnable {
                                             *     Code for posting the task to main thread.
                                             *     eg. textView.setText("Update UI from background thread")
                                             * })
                                             *
                                             * or another short cut to do same is:
                                             *
                                             *textView.post(Runnable {
                                             * textView.setText("Update UI from background thread")
                                             * })
                                             *
                                             */
                                            counterState += 1

                                            Thread.sleep(1000)
                                            if (counterState > 10) {
                                                /**
                                                 * Modern ways to suspend/stop a thread are by using a boolean flag and Thread.interrupt() method.
                                                 *
                                                 */

                                                Thread.currentThread()
                                                    .interrupt()//This will end the running process.
                                            }
                                            if (counterState > 20) {
                                                break
                                            }
                                        }
                                    }).start()

                                }) {
                                    Text(text = "Start loop")
                                }

                                Button(onClick = {
                                    //This click itself/lambda will be processed/executed by
                                    // main looper because it is separate from above infinite thread.
                                    // So Thread.sleep(2000) will be executed on main thread
                                    // since it is free to process the request.
                                    counterState += 1
                                    Log.d(
                                        TAG,
                                        "Stop loop clicked:  $counterState on Thread name- ${Thread.currentThread().name}, Id- ${Thread.currentThread().id}"
                                    )
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
            Intent(context, TaskOnSeparateThread::class.java).apply {
                putExtra(THREAD_ID, threadId)
            }.also {
                context.startActivity(it)
            }
        }
    }
}