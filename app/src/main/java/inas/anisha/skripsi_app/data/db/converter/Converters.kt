package inas.anisha.skripsi_app.data.db.converter

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? {
        if (value == null) return null

        val cal = GregorianCalendar()
        cal.timeInMillis = value
        return cal
    }

    @TypeConverter
    fun toTimestamp(timestamp: Calendar?): Long? {
        if (timestamp == null) return null

        return timestamp.timeInMillis
    }

    @TypeConverter
    fun fromString(stringListString: String): List<String> {
        return stringListString.split(",").map { it }
    }

    @TypeConverter
    fun toString(stringList: List<String>): String {
        return stringList.joinToString(separator = ",")
    }
}