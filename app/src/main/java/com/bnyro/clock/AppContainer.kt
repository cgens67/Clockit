package com.clockit.cgens67

import com.clockit.cgens67.data.database.AppDatabase
import com.clockit.cgens67.domain.repository.AlarmRepository
import com.clockit.cgens67.domain.repository.TimezoneRepository

class AppContainer(database: AppDatabase) {
    val alarmRepository: AlarmRepository by lazy {
        AlarmRepository(database.alarmsDao())
    }
    val timezoneRepository: TimezoneRepository by lazy {
        TimezoneRepository(database.timeZonesDao())
    }
}