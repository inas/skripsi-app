package inas.anisha.skripsi_app.data.db.entity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.Gson
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.ui.common.AlarmReceiver
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toDateString
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toTimeString
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(
    tableName = "reminder", foreignKeys = [ForeignKey(
        onDelete = ForeignKey.CASCADE,
        entity = ScheduleEntity::class,
        parentColumns = ["id"],
        childColumns = ["schedule_id"]
    )]
)
@Parcelize
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "amount") val amount: Int,
    @ColumnInfo(name = "unit") val unit: Int = SkripsiConstant.SCHEDULE_REMINDER_UNIT_MINUTES,
    @ColumnInfo(name = "is_popup") val isPopup: Boolean,
    @ColumnInfo(name = "schedule_id") val scheduleId: Long,
    @ColumnInfo(name = "schedule_data") val scheduleData: String
) : Parcelable {

    fun scheduleReminder(context: Context, userName: String) {
        val schedule: ScheduleEntity = Gson().fromJson(scheduleData, ScheduleEntity::class.java)
        val reminderTime = calculateReminderTime(schedule)

        if (reminderTime > Calendar.getInstance()) {
            val alarmManager: AlarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val pendingIntent = createPendingIntent(context, schedule, userName)
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime.timeInMillis, pendingIntent)
        }
    }

    private fun createPendingIntent(
        context: Context,
        schedule: ScheduleEntity,
        userName: String
    ): PendingIntent {
        val title = when (schedule.type) {
            SkripsiConstant.SCHEDULE_TYPE_TASK -> userName + ", ada tugas yang perlu dikerjakan"
            SkripsiConstant.SCHEDULE_TYPE_TEST -> userName + ", kamu perlu belajar untuk ujian"
            else -> userName + ", jangan lupa dengan kegiatan ini"
        }

        val content = SkripsiConstant.getScheduleTypeString(schedule.type) + " " + schedule.name +
                " untuk " + schedule.startDate.toDateString() + ", " + schedule.startDate.toTimeString()

        val intent = Intent(context, AlarmReceiver::class.java)
        intent.action = AlarmReceiver.ACTION_REMINDER
        intent.putExtra(AlarmReceiver.EXTRA_SCHEDULE_ID, schedule.id.toInt())
        intent.putExtra(AlarmReceiver.EXTRA_TIME, schedule.startDate.timeInMillis)
        intent.putExtra(AlarmReceiver.EXTRA_TITLE, title)
        intent.putExtra(AlarmReceiver.EXTRA_CONTENT, content)
        intent.putExtra(AlarmReceiver.EXTRA_POPUP, isPopup)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            schedule.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return pendingIntent
    }

    fun calculateReminderTime(schedule: ScheduleEntity): Calendar =
        Calendar.getInstance().apply {
            timeInMillis = schedule.startDate.timeInMillis
            add(unit, -amount)
        }

}