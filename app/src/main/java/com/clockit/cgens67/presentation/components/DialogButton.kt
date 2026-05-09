package com.clockit.cgens67.presentation.components

import android.view.HapticFeedbackConstants
import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource

@Composable
fun DialogButton(
    @StringRes label: Int,
    onClick: () -> Unit
) {
    val view = LocalView.current
    TextButton(onClick = {
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        onClick()
    }) {
        Text(text = stringResource(id = label))
    }
}