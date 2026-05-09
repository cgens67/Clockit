package com.clockit.cgens67.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.provider.AlarmClock
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.clockit.cgens67.domain.model.Alarm
import com.clockit.cgens67.navigation.HomeRoutes
import com.clockit.cgens67.navigation.MainNavContainer
import com.clockit.cgens67.navigation.NavRoutes
import com.clockit.cgens67.navigation.homeRoutes
import com.clockit.cgens67.presentation.features.AlarmReceiverDialog
import com.clockit.cgens67.presentation.features.TimerReceiverDialog
import com.clockit.cgens67.presentation.screens.permission.PermissionModel
import com.clockit.cgens67.presentation.screens.settings.model.SettingsModel
import com.clockit.cgens67.presentation.screens.stopwatch.model.StopwatchModel
import com.clockit.cgens67.presentation.screens.timer.model.TimerModel
import com.clockit.cgens67.ui.theme.ClockYouTheme
import com.clockit.cgens67.util.Preferences
import com.clockit.cgens67.util.ThemeUtil
import com.clockit.cgens67.util.services.StopwatchService
import com.clockit.cgens67.util.services.TimerService

class MainActivity : AppCompatActivity() {

    val stopwatchModel by viewModels<StopwatchModel>()
    val timerModel by viewModels<TimerModel>()
    private var initialTab: HomeRoutes = HomeRoutes.Alarm

    lateinit var stopwatchService: StopwatchService

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = (service as StopwatchService.LocalBinder)
            stopwatchService = binder.getService()
            stopwatchModel.state = stopwatchService.state
            stopwatchModel.currentPosition = stopwatchService.currentPosition

            stopwatchService.onStateChange = {
                stopwatchModel.state = it
            }
            stopwatchService.onPositionChange = {
                stopwatchModel.currentPosition = it
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            stopwatchService.onStateChange = {}
            stopwatchService.onPositionChange = {}
        }
    }

    lateinit var timerService: TimerService

    private val timerServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(component: ComponentName, service: IBinder) {
            val binder = (service as TimerService.LocalBinder)
            timerService = binder.getService()
            timerService.onChangeTimers = timerModel::onChangeTimers

            timerModel.onEnqueue = {
                timerService.enqueueNew(it)
            }
            timerModel.updateLabel = timerService::updateLabel
            timerModel.updateRingtone = timerService::updateRingtone
            timerModel.updateVibrate = timerService::updateVibrate

            timerService.invokeChangeListener()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            timerService.onChangeTimers = {}
            timerModel.onEnqueue = null
            timerModel.updateLabel = { _, _ -> }
            timerModel.updateRingtone = { _, _ -> }
            timerModel.updateVibrate = { _, _ -> }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar)
        super.onCreate(savedInstanceState)
        
        // Ensure preferences are loaded
        Preferences.init(applicationContext)

        val hasSeenWelcome = Preferences.instance.getBoolean("hasSeenWelcome", false)
        val requiredPermissions = PermissionModel.allPermissions.any { !it.hasPermission(this) }
        
        val startDestination = when {
            !hasSeenWelcome -> NavRoutes.Welcome.route
            requiredPermissions -> NavRoutes.Permissions.route
            else -> NavRoutes.Home.route
        }

        initialTab = when (intent?.action) {
            SHOW_STOPWATCH_ACTION -> HomeRoutes.Stopwatch
            AlarmClock.ACTION_SET_ALARM, AlarmClock.ACTION_SHOW_ALARMS -> HomeRoutes.Alarm
            AlarmClock.ACTION_SET_TIMER, AlarmClock.ACTION_SHOW_TIMERS -> HomeRoutes.Timer
            else -> homeRoutes.first {
                Preferences.instance.getString(Preferences.startTabKey, HomeRoutes.Alarm.route) == it.route
            }
        }
        
        enableEdgeToEdge()
        setContent {
            val settingsModel: SettingsModel = viewModel()

            val darkTheme = when (settingsModel.themeMode) {
                SettingsModel.Theme.SYSTEM -> isSystemInDarkTheme()
                SettingsModel.Theme.DARK, SettingsModel.Theme.AMOLED -> true
                else -> false
            }
            ClockYouTheme(
                darkTheme = darkTheme,
                customColorScheme = ThemeUtil.getSchemeFromSeed(settingsModel.customColor, darkTheme),
                dynamicColor = settingsModel.colorTheme == SettingsModel.ColorTheme.SYSTEM,
                amoledDark = settingsModel.themeMode == SettingsModel.Theme.AMOLED
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    getInitialAlarm()?.let { AlarmReceiverDialog(this, it) }
                    getInitialTimer()?.let { TimerReceiverDialog(it) }
                    
                    MainNavContainer(settingsModel, initialTab, startDestination)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, StopwatchService::class.java).also { intent ->
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
        Intent(this, TimerService::class.java).also { intent ->
            bindService(intent, timerServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
        unbindService(timerServiceConnection)
    }

    private fun getInitialAlarm(): Alarm? {
        if (intent?.action != AlarmClock.ACTION_SET_ALARM) return null

        val hours = intent.getIntExtra(AlarmClock.EXTRA_HOUR, 0)
        val minutes = intent.getIntExtra(AlarmClock.EXTRA_MINUTES, 0)
        val days = intent.getIntArrayExtra(AlarmClock.EXTRA_DAYS)?.map { it - 1 }
        val ringingTone = intent.getStringExtra(AlarmClock.EXTRA_RINGTONE)
            .takeIf { it != AlarmClock.VALUE_RINGTONE_SILENT }

        return Alarm(
            time = ((hours * 60 + minutes) * 60000).toLong(),
            label = intent.getStringExtra(AlarmClock.EXTRA_MESSAGE),
            enabled = false,
            days = days ?: listOf(0, 1, 2, 3, 4, 5, 6),
            repeat = days != null,
            soundUri = ringingTone,
            vibrate = intent.getBooleanExtra(AlarmClock.EXTRA_VIBRATE, false),
            soundEnabled = ringingTone != null
        )
    }

    private fun getInitialTimer(): Int? {
        if (intent?.action != AlarmClock.ACTION_SET_TIMER) return null

        return intent.getIntExtra(AlarmClock.EXTRA_LENGTH, 0).takeIf { it > 0 }
    }

    companion object {
        const val SHOW_STOPWATCH_ACTION = "com.clockit.cgens67.SHOW_STOPWATCH_ACTION"
    }
}