package com.gregkluska.minesweeper.presentation.ui.homescreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class HomeUiState(
    val text: String
)

sealed interface HomeUiEvent {
    object ButtonClick : HomeUiEvent
}

class HomeViewModel : ViewModel() {

    private val viewModelState: MutableState<HomeUiState> = mutableStateOf(
        HomeUiState(
            text = "Click me"
        )
    )

    val state: State<HomeUiState>
        get() = viewModelState

    fun handleEvent(event: HomeUiEvent) = when (event) {
        HomeUiEvent.ButtonClick -> {}
    }

}