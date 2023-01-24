package com.example.neoweather.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal fun <T> Flow<T>.collectValue(dispatcher: CoroutineDispatcher, action: (T) -> Unit) =
    CoroutineScope(dispatcher).launch {
        this@collectValue.collect { action(it) }
    }