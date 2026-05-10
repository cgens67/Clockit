package com.clockit.cgens67.util.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.clockit.cgens67.util.services.TimerService

class TimerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("receiver", "received timer")
        val id = intent.getIntExtra(TimerService.ID_EXTRA_KEY, 0)
        val serviceIntent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_TIMER_EXPIRED
            putExtra(TimerService.ID_EXTRA_KEY, id)
        }
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}