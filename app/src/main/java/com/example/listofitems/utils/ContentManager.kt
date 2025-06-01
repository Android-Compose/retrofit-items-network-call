package com.example.listofitems.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch



open class ContentManagerViewModel: ViewModel() {
    fun launchCatching(
        block: suspend CoroutineScope.() -> Unit,
        onError: ((Throwable) -> Unit)? = null
    ): Job {
        return viewModelScope.launch {
            try {
                block()
            } catch (e: Throwable) {
                onError?.invoke(e)
            }
        }
    }
}