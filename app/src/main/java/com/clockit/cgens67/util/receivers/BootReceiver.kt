package com.clockit.cgens67.util.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.clockit.cgens67.App
import com.clockit.cgens67.util.AlarmHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmRepository = (context.applicationContext as App).container.alarmRepository
        val alarms = runBlocking(Dispatchers.IO) {
            alarmRepository.getAlarms()
        }
        alarms.forEach {
            AlarmHelper.enqueue(context, it)
        }
    }
}
