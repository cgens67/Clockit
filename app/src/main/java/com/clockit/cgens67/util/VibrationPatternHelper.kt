package com.clockit.cgens67.util

import android.content.Context
import com.clockit.cgens67.domain.model.VibrationPattern
import kotlinx.serialization.json.Json

class VibrationPatternHelper {

    fun getVibrationPatterns(context: Context): List<VibrationPattern> {
        return loadVibrationPatterns(context).entries.map { (name, pattern) ->
            VibrationPattern(name, pattern)
        }
    }

    private fun loadVibrationPatterns(context: Context): Map<String, List<Int>> {
        val vibrationPatterns =
            context.resources.assets.open("vibration_patterns.json").bufferedReader()
                .use { it.readText() }

        val json = Json { ignoreUnknownKeys = true }
        return json.decodeFromString(vibrationPatterns)
    }
}