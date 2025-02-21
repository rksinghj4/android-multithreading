package com.multithreading.customlooper

import android.os.Message
import android.util.Log
import androidx.lifecycle.ViewModel
import com.multithreading.TAG
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CustomLooperViewModel : ViewModel() {
    private var count = 0

    //var customLooper  = CustomLooper()
    var customLooper = CustomLooperHandlerThread("CustomLooperWithHandlerThread")

    var shouldRunThread = true
    private val _progressStateFlow = MutableStateFlow<Int>(0)
    internal val progressStateFlow = _progressStateFlow.asStateFlow()

    private val _resultStateFlow = MutableStateFlow(0)
    internal val resultStateFlow = _resultStateFlow.asStateFlow()

    init {
        customLooper.start()
    }

    fun communicationBetweenThreadToCustomLooper() {
        Thread(Runnable {
            while (shouldRunThread) {
                Thread.sleep(1000)
                count++
                val msg = Message()
                msg.obj = "$count"
                Log.d(
                    TAG,
                    "Communication b/w Thread id: ${Thread.currentThread().id} " +
                            "and customLooper.mHandler.looper: ${customLooper.mHandler?.looper}"
                )
                //It will add the msg to message queue and processed by appropriate looper.
                val isSuccess = customLooper.mHandler?.sendMessage(msg)
                if (count > 15) {
                    break
                }

            }
        }).start()
    }

    fun update() {
        _resultStateFlow.value = count
    }

    fun stop() {
        update()
        shouldRunThread = false
        /**
         * Quits the looper.
         * Causes the loop method to terminate without processing any more messages in the message queue.
         */
        customLooper.mHandler?.looper?.quit()
        //After quit() : customLooper.mHandler?.sendMessage(msg) returns false

    }

}