package com.clockit.cgens67.domain.model

import androidx.annotation.StringRes
import com.clockit.cgens67.R

enum class AlarmSortOrder(@StringRes val value: Int) {
    HOUR_OF_DAY(R.string.hours),
    LABEL(R.string.label),
    WEEKDAY(R.string.weekdays)
}