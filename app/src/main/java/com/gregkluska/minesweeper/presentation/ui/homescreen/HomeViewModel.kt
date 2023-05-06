package com.gregkluska.minesweeper.presentation.ui.homescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class HomeState(
    val text: String
)

sealed interface HomeEvent {
    object ButtonClick : HomeEvent
}

class HomeViewModel : ViewModel() {

    private val viewModelState: MutableState<HomeState> = mutableStateOf(
        HomeState(
            text = "Click me"
        )
    )

    val state: State<HomeState>
        get() = viewModelState

    fun handleEvent(event: HomeEvent) = when (event) {
        HomeEvent.ButtonClick -> {}
    }

}