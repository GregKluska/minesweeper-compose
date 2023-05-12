package com.gregkluska.minesweeper.core

import androidx.annotation.DrawableRes

sealed class DialogState(
    open val onDismiss: () -> Unit
) {

    data class GameOver(
        @DrawableRes val icon: Int,
        val time: Long?,
        val highScore: Long?,
        override val onDismiss: () -> Unit,
        val onTryAgain: () -> Unit
    ) : DialogState(
        onDismiss = onDismiss
    ) {
        companion object {}
    }

}