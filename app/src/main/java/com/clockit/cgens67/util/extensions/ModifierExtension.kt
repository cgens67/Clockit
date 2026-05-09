// Taken from https://github.com/sadellie/unitto/blob/d6f8d2e9127ebaf30c77bdf8d4bbe69348b93481/core/ui/src/main/java/com/sadellie/unitto/core/ui/common/ModifierExtensions.kt#L37
package com.clockit.cgens67.util.extensions

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.squashable(
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource,
    cornerRadiusRange: IntRange,
    role: Role = Role.Button
) = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val cornerRadius by animateIntAsState(
        targetValue = if (isPressed) cornerRadiusRange.first else cornerRadiusRange.last,
        animationSpec = tween(easing = FastOutSlowInEasing)
    )

    Modifier
        .clip(RoundedCornerShape(cornerRadius))
        .combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick,
            interactionSource = interactionSource,
            indication = rememberRipple(),
            role = role,
            enabled = enabled
        )
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.squashable(
    onClick: () -> Unit = {},
    onLongClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource,
    cornerRadiusRange: ClosedRange<Dp>,
    role: Role = Role.Button
) = composed {
    val isPressed by interactionSource.collectIsPressedAsState()
    val cornerRadius: Dp by animateDpAsState(
        targetValue = if (isPressed) cornerRadiusRange.start else cornerRadiusRange.endInclusive,
        animationSpec = tween(easing = FastOutSlowInEasing)
    )

    Modifier
        .clip(RoundedCornerShape(cornerRadius))
        .combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick,
            interactionSource = interactionSource,
            indication = rememberRipple(),
            role = role,
            enabled = enabled
        )
}

fun Modifier.fadingEdge(
    isVisibleTop: Boolean,
    isVisibleBottom: Boolean,
    isVisibleLeft: Boolean = false,
    isVisibleRight: Boolean = false,
    length: Float = 100f
): Modifier = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()

        val colors = listOf(Color.Transparent, Color.Black)
        if (isVisibleTop) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = colors,
                    startY = 0f,
                    endY = length
                ),
                blendMode = BlendMode.DstIn
            )
        }
        if (isVisibleBottom) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = colors.reversed(),
                    startY = size.height - length,
                    endY = size.height
                ),
                blendMode = BlendMode.DstIn
            )
        }
        if (isVisibleLeft) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = colors,
                    startX = 0f,
                    endX = length
                ),
                blendMode = BlendMode.DstIn
            )
        }
        if (isVisibleRight) {
            drawRect(
                brush = Brush.horizontalGradient(
                    colors = colors.reversed(),
                    startX = size.width - length,
                    endX = size.width
                ),
                blendMode = BlendMode.DstIn
            )
        }
    }