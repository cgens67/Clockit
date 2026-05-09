package com.clockit.cgens67.presentation.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.clockit.cgens67.BuildConfig
import com.clockit.cgens67.R
import com.clockit.cgens67.navigation.homeRoutes
import com.clockit.cgens67.presentation.components.ClickableIcon
import com.clockit.cgens67.presentation.screens.settings.components.*
import com.clockit.cgens67.presentation.screens.settings.model.SettingsModel
import com.clockit.cgens67.presentation.screens.timer.model.TimerModel
import com.clockit.cgens67.util.Preferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onClickBack: () -> Unit,
    onNavigateLanguage: () -> Unit,
    settingsModel: SettingsModel,
    timerModel: TimerModel
) {
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    ClickableIcon(imageVector = Icons.AutoMirrored.Filled.ArrowBack) {
                        onClickBack()
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { pv ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(pv)
                .com.clockit.cgens67.util.extensions.fadingEdge(
                    isVisibleTop = scrollState.canScrollBackward,
                    isVisibleBottom = scrollState.canScrollForward,
                    length = 100f
                )
                .verticalScroll(scrollState)
        ) {
            val uriHandler = LocalUriHandler.current

            // General & Localization
            SettingsCategory("General")
            IconPreference(
                title = "Language",
                summary = "Change app language",
                imageVector = Icons.Default.Language
            ) {
                onNavigateLanguage()
            }
            ButtonGroupPref(
                title = "Time Format",
                options = listOf("System", "12h", "24h"),
                values = listOf("System", "12h", "24h"),
                currentValue = settingsModel.timeFormatOverride
            ) {
                settingsModel.timeFormatOverride = it
                Preferences.edit { putString("timeFormatOverride", it) }
            }
            ButtonGroupPref(
                title = "First day of week",
                options = listOf("System", "Sunday", "Monday", "Saturday"),
                values = listOf("System", "Sunday", "Monday", "Saturday"),
                currentValue = settingsModel.firstDayOfWeek
            ) {
                settingsModel.firstDayOfWeek = it
                Preferences.edit { putString("firstDayOfWeek", it) }
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceVariant)

            SettingsCategory(stringResource(R.string.appearance))
            ButtonGroupPref(
                title = stringResource(R.string.theme),
                options = SettingsModel.Theme.entries.map { stringResource(it.resId) },
                values = SettingsModel.Theme.entries,
                currentValue = settingsModel.themeMode
            ) {
                settingsModel.themeMode = it
                Preferences.edit { putString(Preferences.themeKey, it.name) }
            }
            ButtonGroupPref(
                title = stringResource(R.string.color_scheme),
                options = SettingsModel.ColorTheme.entries.map { stringResource(it.resId) },
                values = SettingsModel.ColorTheme.entries,
                currentValue = settingsModel.colorTheme
            ) {
                settingsModel.colorTheme = it
                Preferences.edit { putString(Preferences.colorThemeKey, it.name) }
            }
            AnimatedVisibility(visible = settingsModel.colorTheme == SettingsModel.ColorTheme.CATPPUCCIN) {
                ColorPref(
                    selectedColor = settingsModel.customColor,
                    onSelect = {
                        settingsModel.customColor = it
                        Preferences.edit { putInt(Preferences.customColorKey, it) }
                    }
                )
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceVariant)

            SettingsCategory(title = stringResource(R.string.behavior))
            SwitchPref(
                prefKey = "hapticFeedback",
                title = "Haptic Feedback",
                defaultValue = true
            ) { settingsModel.hapticFeedback = it }
            ButtonGroupPref(
                title = stringResource(R.string.start_tab),
                options = homeRoutes.map { stringResource(it.stringRes) },
                values = homeRoutes,
                currentValue = settingsModel.homeTab
            ) {
                settingsModel.homeTab = it
                Preferences.edit { putString(Preferences.startTabKey, it.route) }
            }
            val tabItems = listOf("alarm" to R.string.alarm, "clock" to R.string.clock, "timer" to R.string.timer, "stopwatch" to R.string.stopwatch)
            ButtonGroupPref(
                title = stringResource(R.string.show_clock_bottom_tab),
                options = tabItems.map { stringResource(it.second) },
                values = tabItems.map { it.first },
                currentValue = settingsModel.enabledTabs
            ) { selectedKey ->
                val key = selectedKey as String
                val currentState = Preferences.instance.getBoolean("show_tab_$key", true)
                settingsModel.toggleTab(key, !currentState)
            }
            SwitchPref(prefKey = Preferences.showSecondsKey, title = stringResource(R.string.show_seconds), defaultValue = true)
            SwitchPref(prefKey = Preferences.timerUsePickerKey, title = stringResource(R.string.timer_use_time_picker), defaultValue = false) {
                timerModel.timePickerFakeUnits = 0
                timerModel.timePickerSeconds = 0
            }
            SwitchPref(prefKey = Preferences.timerShowExamplesKey, title = stringResource(R.string.show_timer_quick_selection), defaultValue = true)

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceVariant)

            SettingsCategory(stringResource(R.string.about))
            IconPreference(
                title = stringResource(R.string.source_code),
                summary = stringResource(R.string.source_code_summary),
                imageVector = Icons.AutoMirrored.Filled.OpenInNew
            ) { uriHandler.openUri("https://github.com/you-apps/ClockYou") }
            IconPreference(
                title = "Clockit",
                summary = stringResource(R.string.version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE),
                imageVector = Icons.Default.History
            ) { uriHandler.openUri("https://github.com/you-apps/ClockYou/releases/latest") }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}