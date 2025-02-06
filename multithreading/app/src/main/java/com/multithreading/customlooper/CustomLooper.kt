package com.multithreading.customlooper

import android.os.Handler
import android.os.Handler.Callback
import android.os.Looper
import android.os.Message
import android.util.Log
import com.multithreading.TAG

class CustomLooper : Thread() {
    internal var mHandler: Handler? = null
    override fun run() {
        //super.run()
        Looper.prepare()
        //Any task assign to this handler can pe processed by this looper thread.
        mHandler = Handler(object : Callback {
            override fun handleMessage(msg: Message): Boolean {
                //if other thread send something using : mHandler.sendMessage(Message()), will be accepted here
                Log.d(TAG, "CustomLooper - Thread: id: ${currentThread().id}, msg = ${msg.obj}")
                return true
            }
        }
        )

        //We should not make singleton Handler
        /*mHandler = object :Handler() {
            override fun handleMessage(msg: Message) {//return type Unit
                super.handleMessage(msg)
            }
        }*/


        Looper.loop()
    }
}