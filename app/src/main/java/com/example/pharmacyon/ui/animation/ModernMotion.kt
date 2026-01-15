package com.example.pharmacyon.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures

/**
 * Small reusable “modern app” press effect.
 *
 * Usage:
 * `Modifier.bouncyClick { ... }`
 */
fun Modifier.bouncyClick(
    enabled: Boolean = true,
    pressedScale: Float = 0.97f,
    onClick: () -> Unit
): Modifier = composed {
    if (!enabled) return@composed this

    val scale = remember { Animatable(1f) }

    this
        .scale(scale.value)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    // Press in
                    scale.animateTo(
                        pressedScale,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )

                    val released = tryAwaitRelease()

                    // Release back
                    scale.animateTo(
                        1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )

                    if (released) onClick()
                }
            )
        }
}

/**
 * Press-only bouncy effect (no click handling).
 *
 * Useful for composables that already have their own `onClick` param (e.g. Button).
 */
fun Modifier.bouncyPress(
    enabled: Boolean = true,
    pressedScale: Float = 0.97f,
): Modifier = composed {
    if (!enabled) return@composed this

    val scale = remember { Animatable(1f) }

    this
        .scale(scale.value)
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    scale.animateTo(
                        pressedScale,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    tryAwaitRelease()
                    scale.animateTo(
                        1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                }
            )
        }
}
