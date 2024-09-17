package com.tgyuu.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tgyuu.domain.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getArticlesUseCase: ArticleRepository,
) : ViewModel() {
    private val intents = Channel<MainIntent>()
    private val sideEffects = Channel<MainSideEffect>()

    val mainState = intents.receiveAsFlow()
        .runningFold(MainState(), ::updateState)
        .stateIn(viewModelScope, SharingStarted.Eagerly, MainState())

    val mainSideEffect = sideEffects.receiveAsFlow()

    internal fun event(event: MainIntent) = viewModelScope.launch {
        intents.send(event)
    }

    internal fun sideEffect(sideEffect: MainSideEffect) = viewModelScope.launch {
        sideEffects.send(sideEffect)
    }

    private suspend fun updateState(current: MainState, event: MainIntent): MainState {
        Log.d("test", "intent 호출 : $event")

        return when (event) {
            MainIntent.Plus -> current.copy(number = current.number + 1)
            MainIntent.Minus -> current.copy(number = current.number - 1)
            MainIntent.GetArticles -> {
                fetchArticles()
                current.copy(isLoading = true)
            }

            is MainIntent.Loaded -> current.copy(isLoading = false, articles = event.articles)
        }
    }

    private fun fetchArticles() = viewModelScope.launch {
        try {
            val articles = getArticlesUseCase.getArticles()
            event(MainIntent.Loaded(articles))
        } catch (e: Exception) {
            sideEffect(MainSideEffect.ShowToast("Failed to load articles"))
        }
    }
}

data class MainState(
    val isLoading: Boolean = false,
    val number: Int = 0,
    val articles: List<String> = emptyList(),
)

sealed interface MainIntent {
    data class Loaded(val articles: List<String>) : MainIntent
    data object Plus : MainIntent
    data object Minus : MainIntent
    data object GetArticles : MainIntent
}

sealed interface MainSideEffect {
    data class ShowToast(val msg: String) : MainSideEffect
}