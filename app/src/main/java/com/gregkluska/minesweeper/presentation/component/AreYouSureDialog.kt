package com.gregkluska.minesweeper.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.gregkluska.minesweeper.R

@Composable
fun AreYouSureDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String = stringResource(R.string.are_you_sure),
    text: String = stringResource(R.string.you_will_lose_your_progress),
    confirmLabel: String = stringResource(R.string.yes),
    dismissLabel: String = stringResource(R.string.no)
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onConfirm,
                content = { Text(confirmLabel) }
            )
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                content = { Text(dismissLabel) }
            )
        },
        title = { Text(title) },
        text = { Text(text) }
    )
}