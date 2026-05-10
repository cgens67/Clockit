package com.clockit.cgens67.presentation.screens.permission

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.clockit.cgens67.domain.model.Permission
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class PermissionModel(application: Application) : AndroidViewModel(application) {
    
    val missingPermissions: StateFlow<List<Permission>> = flow {
        while (true) {
            emit(allPermissions.filter { !it.hasPermission(application) })
            delay(500)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = allPermissions.filter { !it.hasPermission(application) }
    )

    companion object {
        val allPermissions = listOf(
            Permission.AlarmPermission,
            Permission.NotificationPermission,
            Permission.FullScreenIntentPermission,
            Permission.BatteryOptimizationPermission
        )
    }
}