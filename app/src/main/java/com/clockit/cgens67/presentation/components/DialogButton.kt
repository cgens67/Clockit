package com.clockit.cgens67.presentation.components

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import com.clockit.cgens67.util.extensions.performHaptic

@Composable
fun DialogButton(
    @StringRes label: Int,
    onClick: () -> Unit
) {
    val view = LocalView.current
    TextButton(onClick = {
        view.performHaptic()
        onClick()
    }) {
        Text(text = stringResource(id = label))
    }
}