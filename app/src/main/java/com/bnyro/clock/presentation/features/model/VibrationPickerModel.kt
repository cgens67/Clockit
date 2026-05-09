package com.clockit.cgens67.presentation.features.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.clockit.cgens67.domain.model.VibrationPattern
import com.clockit.cgens67.util.VibrationPatternHelper

class VibrationPickerModel(context: Context) : ViewModel() {
    val vibrationPatterns: List<VibrationPattern> =
        VibrationPatternHelper().getVibrationPatterns(context)

    companion object {
        val Factory = viewModelFactory {
            initializer {
                VibrationPickerModel((this[APPLICATION_KEY] as Application).applicationContext)
            }
        }
    }
}