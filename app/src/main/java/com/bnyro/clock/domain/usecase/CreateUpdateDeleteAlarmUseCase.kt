package com.clockit.cgens67.domain.usecase

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.clockit.cgens67.domain.model.Alarm
import com.clockit.cgens67.domain.repository.AlarmRepository
import com.clockit.cgens67.util.AlarmHelper

class CreateUpdateDeleteAlarmUseCase(
    private val context: Context,
    private val alarmRepository: AlarmRepository
) {
    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun createAlarm(alarm: Alarm) {
        // fixx maybe baby D:
        val newId = alarmRepository.addAlarm(alarm)
        val alarmWithId = alarm.copy(id = newId)
        AlarmHelper.enqueue(context, alarmWithId)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun updateAlarm(alarm: Alarm) {

        alarmRepository.updateAlarm(alarm)
        AlarmHelper.enqueue(context, alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {

        alarmRepository.deleteAlarm(alarm)
        AlarmHelper.cancel(context, alarm)
    }
}