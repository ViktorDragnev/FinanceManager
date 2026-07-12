package com.example.csoftproject.data.database.converters

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter

class ColorConverter {

    @TypeConverter
    fun fromColorToHex(color: Color?): String? {
        return color?.let {
            String.format("#%08X", it.toArgb())
        }
    }

    @TypeConverter
    fun fromHexToColor(hex: String?): Color? {
        return hex?.let {
            try {
                Color(parseColor(it))
            } catch (e: Exception) {
                Color.White
            }
        }
    }
}