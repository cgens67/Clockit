package com.clockit.cgens67.domain.repository

import com.clockit.cgens67.data.database.dao.AlarmsDao
import com.clockit.cgens67.domain.model.Alarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AlarmRepository(private val alarmsDao: AlarmsDao) {
    suspend fun addAlarm(alarm: Alarm) = withContext(Dispatchers.IO) { alarmsDao.insert(alarm) }

    suspend fun getAlarms(): List<Alarm> = withContext(Dispatchers.IO) { alarmsDao.getAll() }
    fun getAlarmsStream(): Flow<List<Alarm>> = alarmsDao.getAllStream()

    suspend fun getAlarmById(id: Long): Alarm? =
        withContext(Dispatchers.IO) { alarmsDao.findById(id) }

    suspend fun deleteAlarm(alarm: Alarm) = withContext(Dispatchers.IO) { alarmsDao.delete(alarm) }

    suspend fun updateAlarm(alarm: Alarm) = withContext(Dispatchers.IO) { alarmsDao.update(alarm) }

}