package com.multithreading.asynctask

import android.os.AsyncTask
import android.util.Log
import com.multithreading.LONG_RUNNING_TASK_TIMEOUT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import java.io.IOException

/**
 *
 * AsyncTask is designed to be a helper class around Thread and Handler and does not constitute
 * a generic threading framework. AsyncTasks should ideally be used for
 * short operations (a few seconds at the most.) If you need to keep threads running
 * for long periods of time, it is highly recommended you use the various APIs provided
 * by the java.util.concurrent package such as Executor, ThreadPoolExecutor and FutureTask.
 *
 * Read the source code:
 *
 * AsyncTask<Params, Progress, Result>
 *
 *     @MainThread
 *     protected void onPreExecute() {
 *     }
 *
 *     @WorkerThread
 *     protected abstract Result doInBackground(Params... params);
 *
 *     @WorkerThread
 *     protected final void publishProgress(Progress... values) { }
 *
 *     @MainThread
 *     protected void onProgressUpdate(Progress... values) { }
 *
 *     @MainThread
 *     protected void onPostExecute(Result result) {
 *     }
 *
 *     @MainThread
 *     protected void onCancelled(Result result) {
 *         onCancelled();
 *     }
 *
 */


class AsyncTaskImpl(
    private val coroutineScope: CoroutineScope,
    private val onProgress: (Int?) -> Unit,
    private val onComplete: (Int?) -> Unit,
    private val onError: (String) -> Unit
) : AsyncTask<Int, Int, Int>() {
    private var counter: Int = -1
    private var shouldRunLongTask = true

    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        super.onPreExecute()
        counter = 1
        Log.d(
            TAG,
            "onPreExecute - ThreadId: ${Thread.currentThread().id}, name: ${Thread.currentThread().name}"
        )
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Int?): Int {
        counter = 0
        Log.d(
            TAG,
            "doInBackground - ThreadId: ${Thread.currentThread().id}, " +
                    "name: ${Thread.currentThread().name}"
        )
        return runBlocking {
            try {
                while (shouldRunLongTask) {
                    delay(1000)
                    Log.d(
                        TAG,
                        "doInBackground: in while : ThreadId: ${Thread.currentThread().id}, " +
                                "name: ${Thread.currentThread().name},  counter :  $counter"
                    )
                    counter++
                    publishProgress(counter)//send the progress
                    if (isCancelled) {//Used when asyncTask.cancel() is called
                        break
                    }
                    if (counter >= LONG_RUNNING_TASK_TIMEOUT) {
                        break
                    }
                }
            } catch (e: IOException) {
                onError.invoke(e.toString())
                e.printStackTrace()
            }

            return@runBlocking counter
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(values[0])
        /**
         * can be executed: textView.setText("Counter: ${values[0]}")
         */
        onProgress.invoke(values[0])

        Log.d(
            TAG,
            "onProgressUpdate - ThreadId: ${Thread.currentThread().id}, name: ${Thread.currentThread().name}"
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(result: Int) {
        super.onPostExecute(result)
        Log.d(
            TAG,
            "onPostExecute - ThreadId: ${Thread.currentThread().id}," +
                    " name: ${Thread.currentThread().name}, counter: $result"
        )
        /**
         * can be executed: textView.setText("Final Counter: ${result}")
         */
        onComplete.invoke(result)
    }

    /**
     * onPostExecute(result) will not called if we call
     * asyncTask.cancel() -> triggered onCancelled($result).
     * SO update the UI in onCancelled if needed
     */
    //Run on Main thread
    override fun onCancelled(result: Int) {
        super.onCancelled(result)
        Log.d(TAG, "cancel() -> onCancelled($result)")
        onComplete.invoke(result)
        //When asyncTask.cancel() then this method is called. We can update UI if needed.
    }

    fun stopBackgroundTask() {
        shouldRunLongTask = false
    }

    companion object {
        private const val TAG = "AsyncTaskImpl"
    }
}