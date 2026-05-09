package com.clockit.cgens67.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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

    val animDuration = 600
    val animEasing = FastOutSlowInEasing

    NavHost(navController, startDestination = startDestination, modifier = modifier) {
        
        composable(NavRoutes.Welcome.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animDuration, easing = animEasing)) + fadeIn(animationSpec = tween(animDuration, easing = animEasing)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animDuration, easing = animEasing)) + fadeOut(animationSpec = tween(animDuration, easing = animEasing)) }
        ) {
            WelcomeScreen {
                navController.navigate(NavRoutes.Permissions.route) {
                    popUpTo(NavRoutes.Welcome.route) { inclusive = true }
                }
            }
        }
        
        composable(NavRoutes.Home.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animDuration, easing = animEasing)) + fadeIn(animationSpec = tween(animDuration, easing = animEasing)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animDuration, easing = animEasing)) + fadeOut(animationSpec = tween(animDuration, easing = animEasing)) }
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
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animDuration, easing = animEasing)) + fadeIn(animationSpec = tween(animDuration, easing = animEasing)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animDuration, easing = animEasing)) + fadeOut(animationSpec = tween(animDuration, easing = animEasing)) }
        ) {
            SettingsScreen(
                onClickBack = { navController.popBackStack() },
                onNavigateLanguage = { navController.navigate(NavRoutes.LanguagePicker.route) },
                settingsModel = settingsModel,
                timerModel = timerModel
            )
        }
        
        composable(NavRoutes.LanguagePicker.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(animDuration, easing = animEasing)) + fadeIn(animationSpec = tween(animDuration, easing = animEasing)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(animDuration, easing = animEasing)) + fadeOut(animationSpec = tween(animDuration, easing = animEasing)) }
        ) {
            LanguagePickerScreen(onBack = { navController.popBackStack() })
        }

        composable(NavRoutes.AlarmPicker.routeWithArgs, arguments = NavRoutes.AlarmPicker.args,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animDuration, easing = animEasing)) + fadeIn(animationSpec = tween(animDuration, easing = animEasing)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animDuration, easing = animEasing)) + fadeOut(animationSpec = tween(animDuration, easing = animEasing)) }
        ) {
            AlarmPickerScreen { navController.popBackStack() }
        }

        composable(NavRoutes.Permissions.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up, animationSpec = tween(animDuration, easing = animEasing)) + fadeIn(animationSpec = tween(animDuration, easing = animEasing)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down, animationSpec = tween(animDuration, easing = animEasing)) + fadeOut(animationSpec = tween(animDuration, easing = animEasing)) }
        ) {
            PermissionScreen {
                navController.navigate(NavRoutes.Home.route) {
                    popUpTo(NavRoutes.Permissions.route) { inclusive = true }
                }
            }
        }
    }
}