package com.clockit.cgens67.presentation.widgets

import android.content.Context
import android.widget.RemoteViews
import com.clockit.cgens67.R
import com.clockit.cgens67.domain.model.ClockWidgetOptions
import com.clockit.cgens67.presentation.widgets.DigitalClockWidget.Companion.applyDigitalClockWidgetOptions

class DigitalClockWidgetConfig: ClockWidgetConfig() {
    override val defaultOptions: ClockWidgetOptions = DigitalClockWidget.DefaultConfig
    override val widgetLayoutResource: Int = R.layout.digital_clock

    override fun updateClockWidget(context: Context, views: RemoteViews, options: ClockWidgetOptions) {
        views.applyDigitalClockWidgetOptions(context, options)
    }
}