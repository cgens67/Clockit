package com.clockit.cgens67.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.clockit.cgens67.presentation.screens.settings.model.SettingsModel

@Composable
fun MainNavContainer(
    settingsModel: SettingsModel, initialTab: HomeRoutes,
    startDestination: String
) {
    val navController = rememberNavController()
    AppNavHost(
        navController,
        settingsModel,
        initialTab = initialTab,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    )
}
