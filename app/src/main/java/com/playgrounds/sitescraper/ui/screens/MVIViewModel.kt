package com.playgrounds.sitescraper.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class MVIViewModel<STATE, EVENT, ACTION>(emptyState: STATE): ViewModel() {
    protected val _stateFlow = MutableStateFlow(emptyState)
    val stateFlow: StateFlow<STATE> = _stateFlow

    private val _actionFlow = MutableSharedFlow<ACTION>()
    val actionFlow: SharedFlow<ACTION> = _actionFlow
    protected fun emit(action: ACTION) {
        viewModelScope.launch {
            _actionFlow.emit(action)
        }
    }

    abstract fun dispatchEvent(event: EVENT)
}