package com.clockit.cgens67.domain.model

import androidx.annotation.StringRes
import com.clockit.cgens67.R

enum class TimeZoneSortOrder(@StringRes val value: Int) {
    ALPHABETIC(R.string.alphabetic),
    OFFSET(R.string.offset)
}
