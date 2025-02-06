package com.multithreading.asynctask

import android.os.Looper
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread

abstract class CustomAsyncTask<Params, Progress, Result> {

    @MainThread
    fun onPreExecute() {

    }

    @WorkerThread
    abstract fun doInBackground(params: Params): Result

    @WorkerThread //validates that annotated method is called from an appropriate background thread.
    fun publishProgress(progress: Progress) {

    }

    @MainThread
    fun onProgressUpdate(progress: Progress) {

    }

    @MainThread
    fun postExecute(result: Result) {

    }

    @MainThread
    fun onCancel() {

    }

    fun ensureMainThread(methodName: String) {
        require(Looper.myLooper() == Looper.getMainLooper()) {
            throw IllegalStateException("Can not invoke $methodName on background")
        }
    }

    fun execute() {

    }


    companion object {
        const val TAG = "CustomAsyncTask"
    }
}