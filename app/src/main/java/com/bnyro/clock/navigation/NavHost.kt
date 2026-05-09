package com.clockit.cgens67.navigation

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.clockit.cgens67.presentation.screens.alarm.model.AlarmModel
import com.clockit.cgens67.presentation.screens.alarmpicker.AlarmPickerScreen
import com.clockit.cgens67.presentation.screens.clock.model.ClockModel
import com.clockit.cgens67.presentation.screens.permission.PermissionScreen
import com.clockit.cgens67.presentation.screens.settings.LanguagePickerScreen
import com.clockit.cgens67.presentation.screens.settings.SettingsScreen
import com.clockit.cgens67.presentation.screens.settings.model.SettingsModel
import com.clockit.cgens67.presentation.screens.stopwatch.model.StopwatchModel
import com.clockit.cgens67.presentation.screens.timer.model.TimerModel
import com.clockit.cgens67.presentation.screens.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    settingsModel: SettingsModel,
    initialTab: HomeRoutes,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    val alarmModel: AlarmModel = viewModel()
    val timerModel: TimerModel = viewModel()
    val stopwatchModel: StopwatchModel = viewModel()
    val clockModel: ClockModel = viewModel()

    NavHost(navController, startDestination = startDestination, modifier = modifier) {
        
        composable(NavRoutes.Welcome.route) {
            WelcomeScreen {
                navController.navigate(NavRoutes.Permissions.route) {
                    popUpTo(NavRoutes.Welcome.route) { inclusive = true }
                }
            }
        }
        
        composable(NavRoutes.Home.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down) + fadeIn() },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up) + fadeOut() }
        ) {
            HomeNavContainer(
                onNavigate = { navController.navigate(it) },
                alarmModel = alarmModel,
                clockModel = clockModel,
                timerModel = timerModel,
                stopwatchModel = stopwatchModel,
                initialTab = initialTab,
                settingsModel = settingsModel
            )
        }
        
        composable(NavRoutes.Settings.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) + fadeIn() },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down) + fadeOut() }
        ) {
            SettingsScreen(
                onClickBack = { navController.popBackStack() },
                onNavigateLanguage = { navController.navigate(NavRoutes.LanguagePicker.route) },
                settingsModel = settingsModel,
                timerModel = timerModel
            )
        }
        
        composable(NavRoutes.LanguagePicker.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start) + fadeIn() },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End) + fadeOut() }
        ) {
            LanguagePickerScreen(onBack = { navController.popBackStack() })
        }

        composable(NavRoutes.AlarmPicker.routeWithArgs, arguments = NavRoutes.AlarmPicker.args,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) + fadeIn() },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down) + fadeOut() }
        ) {
            AlarmPickerScreen { navController.popBackStack() }
        }

        composable(NavRoutes.Permissions.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) + fadeIn() },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down) + fadeOut() }
        ) {
            PermissionScreen {
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Permissions.route) { inclusive = true }
                }
            }
        }
    }
}