package com.uesar.journal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class Counter {
    private val _counter = MutableStateFlow(0)
    val counter: StateFlow<Int> = _counter.asStateFlow()
    private var counterJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun startOrResumeCounter() {
        if (counterJob == null || counterJob?.isCancelled == true) {
            counterJob = coroutineScope.launch {
                while (isActive) {
                    _counter.value++
                    delay(1000)
                }
            }
        }
    }

    fun pauseCounter() {
        counterJob?.cancel()
        counterJob = null
    }

    fun resetCounter() {
        pauseCounter()
        _counter.value = 0
    }
}