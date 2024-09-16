package com.tgyuu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val events = Channel<MainEvent>()

    val mainState = events.receiveAsFlow()
        .runningFold(MainState(), ::updateState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, MainState())

    internal fun event(event: MainEvent) = viewModelScope.launch {
        events.send(event)
    }

    private fun updateState(current: MainState, event: MainEvent): MainState {
        return when (event) {
            MainEvent.Plus -> current.copy(number = current.number + 1)
            MainEvent.Minus -> current.copy(number = current.number - 1)
        }
    }
}

data class MainState(
    val isLoading: Boolean = false,
    val number: Int = 0,
)

sealed class MainEvent {
    data object Plus : MainEvent()
    data object Minus : MainEvent()
}