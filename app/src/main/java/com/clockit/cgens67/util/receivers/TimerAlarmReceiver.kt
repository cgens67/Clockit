package com.clockit.cgens67.util.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.clockit.cgens67.util.services.TimerService

class TimerAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("TimerAlarmReceiver", "Received timer expiry exact alarm intent")
        
        val id = intent.getIntExtra(TimerService.ID_EXTRA_KEY, 0)
        val label = intent.getStringExtra("label")
        val ringtone = intent.getStringExtra("ringtone")
        val vibrate = intent.getBooleanExtra("vibrate", false)

        val serviceIntent = Intent(context, TimerService::class.java).apply {
            action = TimerService.ACTION_TIMER_EXPIRED
            putExtra(TimerService.ID_EXTRA_KEY, id)
            putExtra("label", label)
            putExtra("ringtone", ringtone)
            putExtra("vibrate", vibrate)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}