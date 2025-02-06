package com.multithreading.asynctask

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class AsyncTaskViewModel : ViewModel() {

    private val _progressStateFlow = MutableStateFlow(0)
    internal val progressStateFlow = _progressStateFlow.asStateFlow()

    private val _resultStateFlow = MutableStateFlow(0)
    internal val resultStateFlow = _resultStateFlow.asStateFlow()

    private val _errorSharedFlow = MutableSharedFlow<String>()
    internal val errorSharedFlow = _errorSharedFlow.asSharedFlow()

    private val asyncTask = AsyncTaskImpl(CoroutineScope(SupervisorJob()), onProgress = {
        _progressStateFlow.value = (it ?: 0)
    }, onComplete = {
        _resultStateFlow.value = it ?: 0
    }, onError = {
        flow<String> { _errorSharedFlow.emit(it) }
    })

    init {
        fetchData()
    }

    private fun fetchData() {
        asyncTask.execute()
    }

    fun stopLongRunningTask() {
        asyncTask.cancel(false)
        //asyncTask.cancel(true) //Interrupt the thread

        //asyncTask.stopBackgroundTask()
    }
}