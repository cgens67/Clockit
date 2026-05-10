package com.clockit.cgens67.domain.model

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import com.clockit.cgens67.R

sealed class Permission(
    val title: String,
    val description: String,
    @DrawableRes val iconRes: Int
) {
    abstract fun hasPermission(context: Context): Boolean
    abstract fun requestPermission(activity: Activity)

    object NotificationPermission : Permission(
        title = "Notifications",
        description = "Required to show alarms and timers when they go off.",
        iconRes = R.drawable.ic_notification
    ) {
        override fun hasPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return true
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }

        override fun requestPermission(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1
            )
        }
    }

    object AlarmPermission : Permission(
        title = "Alarms & Reminders",
        description = "Required to schedule alarms and timers precisely at exact times.",
        iconRes = R.drawable.ic_alarm
    ) {
        override fun hasPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return true
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            return alarmManager.canScheduleExactAlarms()
        }

        override fun requestPermission(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = android.net.Uri.parse("package:${activity.packageName}")
            }
            activity.startActivity(intent)
        }
    }

    object FullScreenIntentPermission : Permission(
        title = "Full Screen Wakeup",
        description = "Allows alarms to wake up the screen and display the stop or snooze controls over the lockscreen.",
        iconRes = R.drawable.ic_alarm
    ) {
        override fun hasPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) return true
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            return notificationManager.canUseFullScreenIntent()
        }

        override fun requestPermission(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) return
            val intent = Intent(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT).apply {
                data = android.net.Uri.parse("package:${activity.packageName}")
            }
            activity.startActivity(intent)
        }
    }

    object BatteryOptimizationPermission : Permission(
        title = "Unrestricted Battery",
        description = "Ensures that Android's aggressive battery optimizations don't randomly delay your alarms.",
        iconRes = R.drawable.ic_alarm
    ) {
        override fun hasPermission(context: Context): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
            val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            return pm.isIgnoringBatteryOptimizations(context.packageName)
        }

        override fun requestPermission(activity: Activity) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = android.net.Uri.parse("package:${activity.packageName}")
            }
            activity.startActivity(intent)
        }
    }
}