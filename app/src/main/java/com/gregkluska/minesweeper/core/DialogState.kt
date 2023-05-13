package com.gregkluska.minesweeper.core

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.gregkluska.minesweeper.R

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

    data class AreYouSure(
        @StringRes val title: Int = R.string.are_you_sure,
        @StringRes val text: Int = R.string.you_will_lose_your_progress,
        val onConfirm: () -> Unit,
        override val onDismiss: () -> Unit
    ) : DialogState(
        onDismiss = onDismiss
    )

}