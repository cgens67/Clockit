package com.clockit.cgens67.presentation.screens.permission

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.clockit.cgens67.domain.model.Permission

class PermissionModel(application: Application) :
    AndroidViewModel(application) {
    val requiredPermissions = allPermissions.filter {
        !it.hasPermission(application)
    }

    companion object {
        val allPermissions = listOf(
            Permission.AlarmPermission,
            Permission.NotificationPermission,
            Permission.BatteryOptimizationPermission,
            Permission.AllDonePermission

        )
    }
}