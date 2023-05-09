package com.gregkluska.minesweeper.presentation.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gregkluska.minesweeper.core.AppBarState

class MainViewModel : ViewModel() {

    val _appBarState = mutableStateOf<AppBarState>(AppBarState.Hide)
    val appBarState: State<AppBarState>
        get() = _appBarState


}