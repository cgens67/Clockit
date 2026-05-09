package com.clockit.cgens67.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import com.clockit.cgens67.util.extensions.performHaptic

@Composable
fun ClickableIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val view = LocalView.current
    IconButton(modifier = modifier, onClick = {
        view.performHaptic()
        onClick()
    }) {
        Icon(imageVector, contentDescription)
    }
}