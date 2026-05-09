package com.clockit.cgens67.presentation.screens.settings.model

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.clockit.cgens67.R
import com.clockit.cgens67.navigation.HomeRoutes
import com.clockit.cgens67.navigation.homeRoutes
import com.clockit.cgens67.util.Preferences
import com.clockit.cgens67.util.catpucchinLatte

class SettingsModel : ViewModel() {
    enum class Theme(@StringRes val resId: Int) {
        SYSTEM(R.string.system), LIGHT(R.string.light), DARK(R.string.dark), AMOLED(R.string.amoled)
    }

    enum class ColorTheme(@StringRes val resId: Int) {
        SYSTEM(R.string.system), CATPPUCCIN(R.string.catppuccin)
    }

    var themeMode: Theme by mutableStateOf(Theme.valueOf(Preferences.instance.getString(Preferences.themeKey, Theme.SYSTEM.name) ?: Theme.SYSTEM.name))
    var colorTheme: ColorTheme by mutableStateOf(ColorTheme.valueOf(Preferences.instance.getString(Preferences.colorThemeKey, ColorTheme.SYSTEM.name) ?: ColorTheme.SYSTEM.name))
    var customColor by mutableStateOf(Preferences.instance.getInt(Preferences.customColorKey, catpucchinLatte.first()))
    
    // New Expressive Settings
    var hapticFeedback by mutableStateOf(Preferences.instance.getBoolean("hapticFeedback", true))
    var timeFormatOverride by mutableStateOf(Preferences.instance.getString("timeFormatOverride", "System") ?: "System")
    var firstDayOfWeek by mutableStateOf(Preferences.instance.getString("firstDayOfWeek", "System") ?: "System")

    var enabledTabs by mutableStateOf(
        homeRoutes.mapNotNull { route ->
            route.route.takeIf { Preferences.instance.getBoolean("show_tab_${route.route}", true) }
        }
    )

    var homeTab by mutableStateOf(
        homeRoutes.first {
            it.route == Preferences.instance.getString(Preferences.startTabKey, HomeRoutes.Alarm.route)
        }
    )

    fun toggleTab(route: String, enabled: Boolean) {
        Preferences.edit { putBoolean("show_tab_$route", enabled) }
        enabledTabs = homeRoutes.mapNotNull { r ->
            r.route.takeIf { Preferences.instance.getBoolean("show_tab_${r.route}", true) }
        }
    }
}