package com.clockit.cgens67.presentation.widgets

import android.content.Context
import android.widget.RemoteViews
import com.clockit.cgens67.R
import com.clockit.cgens67.domain.model.ClockWidgetOptions
import com.clockit.cgens67.presentation.widgets.VerticalClockWidget.Companion.applyVerticalClockWidgetOptions

class VerticalClockWidgetConfig: ClockWidgetConfig() {
    override val defaultOptions: ClockWidgetOptions = VerticalClockWidget.DefaultConfig
    override val widgetLayoutResource: Int = R.layout.vertical_clock

    override fun updateClockWidget(context: Context, views: RemoteViews, options: ClockWidgetOptions) {
        views.applyVerticalClockWidgetOptions(context, options)
    }
}