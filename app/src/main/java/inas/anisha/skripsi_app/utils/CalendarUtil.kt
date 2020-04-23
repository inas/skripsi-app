package inas.anisha.skripsi_app.utils

import java.text.SimpleDateFormat
import java.util.*

class CalendarUtil {

    companion object {
        fun Calendar.toPreviousMidnight(): Calendar {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            return this
        }

        fun Calendar.toNextMidnight(): Calendar {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_MONTH, 1)
            return this
        }

        fun getDateDisplay(date: Calendar): String {
            val dateFormat = SimpleDateFormat("EEEE, dd LLLL", Locale.getDefault())
            return dateFormat.format(date.timeInMillis)
        }

        fun Calendar.toTimeString(): String {
            val dateFormat = SimpleDateFormat("HH.mm", Locale.getDefault())
            return dateFormat.format(timeInMillis)
        }

        fun Calendar.toMinuteOfDay(): Int {
            val hour = get(Calendar.HOUR_OF_DAY)
            val minute = get(Calendar.MINUTE)
            return ((hour * 60) + minute)
        }

        fun fromMinuteToTimeString(minutes: Int): String {
            val hour = minutes / 60
            val minute = minutes % 60

            return Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }.toTimeString()
        }
    }
}