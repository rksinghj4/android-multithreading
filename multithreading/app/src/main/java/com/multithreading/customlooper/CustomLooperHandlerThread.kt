package com.multithreading.customlooper

import android.os.Handler
import android.os.Handler.Callback
import android.os.HandlerThread
import android.os.Message
import android.util.Log
import com.multithreading.TAG

class CustomLooperHandlerThread(val handlerName: String) : HandlerThread(handlerName) {
    internal var mHandler: Handler? = null

    /**
     * If we use HandlerThread then we don't need to prepare looper as follow
     * Looper.prepare()
     * mHandler =
     * Looper.loop()
     */
    override fun onLooperPrepared() {
        super.onLooperPrepared() //mmHandler should be after super.onLooperPrepared()
        mHandler = Handler(object : Callback {
            override fun handleMessage(msg: Message): Boolean {
                Log.d(
                    TAG,
                    "CustomLooperHandlerThread - Thread: id: ${currentThread().id}, msg = ${msg.obj}"
                )
                return true
            }
        })
    }
}