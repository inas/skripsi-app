package inas.anisha.skripsi_app.ui.main.schedule.schedule

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.ReminderEntity
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.ui.common.AlarmReceiver
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toDateString
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toTimeString
import java.util.*


class ScheduleDetailViewModel(val mApplication: Application) : AndroidViewModel(mApplication) {

    val mRepository: Repository = Repository.getInstance(mApplication)

    var schedule: ScheduleEntity = ScheduleEntity(0)

    fun getSchedule(scheduleId: Long): LiveData<ScheduleEntity> =
        mRepository.getSchedule(scheduleId)

    fun updateScheduleAsComplete(isComplete: Boolean) {
        val isOnTime = Calendar.getInstance() <= schedule.endDate
        mRepository.updateScheduleAsComplete(schedule.id, isComplete)
        mRepository.updateScheduleOnTimeStatus(schedule.id, isOnTime)
    }

    fun updateSchedule(scheduleVm: ScheduleViewModel) {
        schedule = scheduleVm.toEntity()
        scheduleVm.reminder?.let { scheduleReminder(it) }
        mRepository.updateSchedule(scheduleVm.toEntity())
    }

    fun deleteTask() = mRepository.deleteSchedule(schedule.id)

    fun scheduleReminder(reminder: ReminderEntity) {
        val alarmManager: AlarmManager =
            mApplication.getSystemService(ALARM_SERVICE) as AlarmManager
        val pendingIntent = createPendingIntent(reminder)
        val reminderTime = calculateReminderTime(reminder)
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminderTime.timeInMillis, pendingIntent)
    }

    fun createPendingIntent(reminder: ReminderEntity): PendingIntent {
        val intent = Intent(mApplication, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            mApplication,
            reminder.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val userName = mRepository.getUserName()
        val title = when (schedule.type) {
            SkripsiConstant.SCHEDULE_TYPE_TASK -> userName + ", ada tugas yang perlu dikerjakan"
            SkripsiConstant.SCHEDULE_TYPE_TEST -> userName + ", kamu perlu belajar untuk ujian"
            else -> userName + ", jangan lupa dengan kegiatan ini"
        }

        val content = SkripsiConstant.getScheduleTypeString(schedule.type) + " " + schedule.name +
                "; " + schedule.startDate.toDateString() + ", " + schedule.startDate.toTimeString()

        intent.putExtra(AlarmReceiver.EXTRA_SCHEDULE_ID, schedule.id)
        intent.putExtra(AlarmReceiver.EXTRA_TIME, schedule.startDate.timeInMillis)
        intent.putExtra(AlarmReceiver.EXTRA_TITLE, title)
        intent.putExtra(AlarmReceiver.EXTRA_CONTENT, content)
        intent.putExtra(AlarmReceiver.EXTRA_POPUP, reminder.isPopup)
        return pendingIntent
    }

    fun calculateReminderTime(reminder: ReminderEntity): Calendar =
        Calendar.getInstance().apply {
            timeInMillis = schedule.startDate.timeInMillis
            add(reminder.unit, -reminder.amount)
        }

}