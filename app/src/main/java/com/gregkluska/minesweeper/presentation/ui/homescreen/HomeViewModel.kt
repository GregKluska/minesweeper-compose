package com.gregkluska.minesweeper.presentation.ui.homescreen

import androidx.lifecycle.ViewModel

sealed interface HomeUiEvent {
    data class GameStart(
        val width: Int,
        val height: Int,
        val mines: Int
    ) : HomeUiEvent
}

class HomeViewModel : ViewModel() {

}