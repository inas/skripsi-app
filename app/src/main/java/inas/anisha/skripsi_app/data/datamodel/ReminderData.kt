package inas.anisha.skripsi_app.data.datamodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.ui.common.AlarmReceiver
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toDateString
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toTimeString
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ReminderData(
    var startDateMillis: Long = 0,
    var title: String = "",
    var content: String = "",
    var scheduleType: Int = 0,
    var scheduleId: Int = 0,
    var amount: Int = 1,
    var unit: Int = SkripsiConstant.SCHEDULE_REMINDER_UNIT_MINUTES,
    val isPopup: Boolean = false
) : Parcelable {

    fun addScheduleData(schedule: ScheduleEntity): ReminderData {
        startDateMillis = schedule.startDate.timeInMillis

        title = when (schedule.type) {
            SkripsiConstant.SCHEDULE_TYPE_TASK -> ", ada tugas yang perlu dikerjakan"
            SkripsiConstant.SCHEDULE_TYPE_TEST -> ", kamu perlu belajar untuk ujian"
            else -> ", jangan lupa dengan kegiatan ini"
        }

        content =
            "(" + SkripsiConstant.getScheduleTypeString(schedule.type) + ") " + schedule.name +
                " untuk " + schedule.startDate.toDateString() + ", " + schedule.startDate.toTimeString()

        scheduleType = schedule.type
        scheduleId = schedule.id.toInt()
        return this
    }

    fun scheduleReminder(context: Context) {
        val reminderTime = calculateReminderTime()

        if (reminderTime > Calendar.getInstance()) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = createPendingIntent(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    reminderTime.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime.timeInMillis, pendingIntent)
            }
        }
    }

    private fun createPendingIntent(context: Context): PendingIntent {

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        intent.action = AlarmReceiver.ACTION_REMINDER
        intent.putExtra(AlarmReceiver.EXTRA_SCHEDULE_ID, scheduleId)
        intent.putExtra(AlarmReceiver.EXTRA_TIME, startDateMillis)
        intent.putExtra(AlarmReceiver.EXTRA_TITLE, title)
        intent.putExtra(AlarmReceiver.EXTRA_CONTENT, content)
        intent.putExtra(AlarmReceiver.EXTRA_POPUP, isPopup)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            scheduleId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return pendingIntent
    }

    fun calculateReminderTime(): Calendar =
        Calendar.getInstance().apply {
            timeInMillis = startDateMillis
            add(unit, -amount)
        }

}