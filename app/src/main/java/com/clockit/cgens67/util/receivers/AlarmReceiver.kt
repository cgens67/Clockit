package com.clockit.cgens67.util.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.clockit.cgens67.App
import com.clockit.cgens67.util.AlarmHelper
import com.clockit.cgens67.util.TimeHelper
import com.clockit.cgens67.util.services.AlarmService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        // Allows operations on background thread while preventing Android from killing the Receiver
        val pendingResult = goAsync()
        val id = intent.getLongExtra(AlarmHelper.EXTRA_ID, -1L).takeIf { it != -1L }
        
        if (id == null) {
            pendingResult.finish()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val alarmRepository = (context.applicationContext as App).container.alarmRepository
                val alarm = alarmRepository.getAlarmById(id) ?: return@launch

                val currentDay = TimeHelper.getCurrentWeekDay()

                // Check if the alarm should fire today
                if ((currentDay - 1) in alarm.days || !alarm.repeat) {
                    val playAlarm = Intent(context, AlarmService::class.java).apply {
                        putExtra(AlarmHelper.EXTRA_ID, id)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ContextCompat.startForegroundService(context, playAlarm)
                    } else {
                        context.startService(playAlarm)
                    }
                }

                // Re-enqueue the alarm for the next day
                if (alarm.repeat) {
                    AlarmHelper.enqueue(context, alarm)
                } else {
                    alarm.enabled = false
                    alarmRepository.updateAlarm(alarm)
                }
            } catch (e: Exception) {
                Log.e("AlarmReceiver", "Failed to process alarm safely", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}