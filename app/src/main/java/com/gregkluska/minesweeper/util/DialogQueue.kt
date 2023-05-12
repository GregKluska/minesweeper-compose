package com.gregkluska.minesweeper.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.gregkluska.minesweeper.core.DialogState
import java.util.LinkedList

class DialogQueue {

    private val dialogQueue = LinkedList<DialogState>()
    private val _dialog: MutableState<DialogState?> = mutableStateOf(dialogQueue.firstOrNull())

    val dialog: State<DialogState?>
        get() = _dialog

    fun appendDialog(state: DialogState) {
        dialogQueue.addLast(state)
        _dialog.value = dialogQueue.peek()
    }

    fun removeHeadDialog() {
        dialogQueue.removeFirstOrNull()
        _dialog.value = dialogQueue.peek()
    }
}