package inas.anisha.skripsi_app.constant

import java.util.*

class SkripsiConstant {
    companion object {
        const val CYCLE_FREQUENCY_DAILY = 111000
        const val CYCLE_FREQUENCY_WEEKLY = 111001
        const val CYCLE_FREQUENCY_MONTHLY = 111002

        const val SCHEDULE_TYPE_TASK = 111010
        const val SCHEDULE_TYPE_TEST = 111011
        const val SCHEDULE_TYPE_ACTIVITY = 111012
        const val SCHEDULE_TYPE_SCHOOL = 111013

        const val SCHEDULE_TIMELINE_TYPE_ACTIVITY = 111020
        const val SCHEDULE_TIMELINE_TYPE_CLASS = 111021

        const val SCHEDULE_REMINDER_UNIT_MINUTES = Calendar.MINUTE
        const val SCHEDULE_REMINDER_UNIT_HOURS = Calendar.HOUR_OF_DAY
        const val SCHEDULE_REMINDER_UNIT_DAYS = Calendar.DAY_OF_MONTH

        const val OVERLAP_SHIFT = 8
        const val MINUTE_HEIGHT = 1.5
        const val BLOCK_HEIGHT = 90
        const val TIME_WIDTH = 40
        const val INDICATOR_WIDTH = 4

        fun getScheduleTypeString(type: Int): String {
            return when (type) {
                SCHEDULE_TYPE_TASK -> "Tugas"
                SCHEDULE_TYPE_TEST -> "Ujian"
                else -> "Kegiatan"
            }
        }

        fun getCycleFrequencyString(frequency: Int): String {
            return when (frequency) {
                CYCLE_FREQUENCY_DAILY -> "Harian"
                CYCLE_FREQUENCY_WEEKLY -> "Mingguan"
                else -> "Bulanan"
            }
        }

        fun getScheduleReminderUnitString(unit: Int): String {
            return when (unit) {
                SCHEDULE_REMINDER_UNIT_MINUTES -> "Menit Sebelumnya"
                SCHEDULE_REMINDER_UNIT_HOURS -> "Jam Sebelumnya"
                else -> "Hari Sebelumnya"
            }
        }
    }
}