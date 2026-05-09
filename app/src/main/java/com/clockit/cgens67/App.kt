package com.clockit.cgens67

import android.app.Application
import com.clockit.cgens67.data.database.AppDatabase
import com.clockit.cgens67.util.NotificationHelper
import com.clockit.cgens67.util.Preferences

class App : Application() {
    lateinit var container: AppContainer
    private val database by lazy { AppDatabase.getDatabase(this) }
    override fun onCreate() {
        super.onCreate()

        Preferences.init(this)
        NotificationHelper.createStaticNotificationChannels(this)

        container = AppContainer(database)
    }
}
