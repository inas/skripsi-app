package inas.anisha.skripsi_app.utils

import java.text.SimpleDateFormat
import java.util.*

class CalendarUtil {

    companion object {
        fun Calendar.getPreviousMidnight(): Calendar {
            val newCalendar = Calendar.getInstance().also { it.timeInMillis = timeInMillis }
            newCalendar.set(Calendar.HOUR_OF_DAY, 0)
            newCalendar.set(Calendar.MINUTE, 0)
            newCalendar.set(Calendar.SECOND, 0)
            newCalendar.set(Calendar.MILLISECOND, 0)
            return newCalendar
        }

        fun Calendar.getNextMidnight(): Calendar {
            val newCalendar = Calendar.getInstance().also { it.timeInMillis = timeInMillis }
            newCalendar.set(Calendar.HOUR_OF_DAY, 0)
            newCalendar.set(Calendar.MINUTE, 0)
            newCalendar.set(Calendar.SECOND, 0)
            newCalendar.set(Calendar.MILLISECOND, 0)
            newCalendar.add(Calendar.DAY_OF_MONTH, 1)
            return newCalendar
        }

        fun getDateDisplayDayDate(date: Calendar): String {
            val dateFormat = SimpleDateFormat("EEEE, d LLLL", Locale.getDefault())
            return dateFormat.format(date.timeInMillis)
        }

        fun getDateDisplayDate(date: Calendar): String {
            val dateFormat = SimpleDateFormat("d LLLL yyyy", Locale.getDefault())
            return dateFormat.format(date.timeInMillis)
        }

        fun getDateDisplayDate(millis: Long): String {
            val dateFormat = SimpleDateFormat("d LLLL yyyy", Locale.getDefault())
            return dateFormat.format(millis)
        }

        fun Calendar.toDayMonthString(): String {
            val dateFormat = SimpleDateFormat("d LLLL", Locale.getDefault())
            return dateFormat.format(timeInMillis)
        }

        fun Calendar.toDateString(): String {
            val dateFormat = SimpleDateFormat("d LLLL yyyy", Locale.getDefault())
            return dateFormat.format(timeInMillis)
        }

        fun Calendar.toTimeString(): String {
            val dateFormat = SimpleDateFormat("HH.mm", Locale.getDefault())
            return dateFormat.format(timeInMillis)
        }

        fun Calendar.getMinuteOfDay(): Int {
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

        fun Calendar.standardized(): Calendar {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            return this
        }

        fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean =
            cal1[Calendar.DAY_OF_YEAR] == cal2[Calendar.DAY_OF_YEAR]
                    && cal1[Calendar.YEAR] == cal2[Calendar.YEAR]

        fun Calendar.isTimeEarlierThan(other: Calendar): Boolean =
            getMinuteOfDay() < other.getMinuteOfDay()
    }
}