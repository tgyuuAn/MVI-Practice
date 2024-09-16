package com.tgyuu.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _number = MutableStateFlow(0)
    val number = _number.asStateFlow()

    internal fun plus() {
        _number.value += 1
    }

    internal fun minus() {
        _number.value -= 1
    }
}