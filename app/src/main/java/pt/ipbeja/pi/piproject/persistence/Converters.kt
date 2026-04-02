package pt.ipbeja.pi.piproject.persistence

import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converters for Room database to handle Date objects.
 */
class Converters {

    /**
     * Converts a timestamp Long value to a Date object.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Converts a Date object to a timestamp Long value.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}