package com.clockit.cgens67.data.database.dao

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun stringToIntList(value: String): List<Int> = value.split(",").mapNotNull { it.toIntOrNull() }

    @TypeConverter
    fun intListToString(value: List<Int>) = value.joinToString(",")
}
