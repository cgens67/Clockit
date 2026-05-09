package com.clockit.cgens67.util.extensions

import android.view.HapticFeedbackConstants
import android.view.View
import com.clockit.cgens67.util.Preferences

/**
 * Extension function to trigger haptic feedback conditionally based on the user's preferences.
 */
fun View.performHaptic(constant: Int = HapticFeedbackConstants.VIRTUAL_KEY) {
    if (Preferences.instance.getBoolean("hapticFeedback", true)) {
        performHapticFeedback(constant)
    }
}