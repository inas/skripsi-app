package inas.anisha.srl_app.ui.main.schedule.schedule

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import inas.anisha.srl_app.constant.SkripsiConstant
import inas.anisha.srl_app.data.datamodel.ReminderData
import inas.anisha.srl_app.data.db.entity.ScheduleEntity
import inas.anisha.srl_app.utils.CalendarUtil
import inas.anisha.srl_app.utils.CalendarUtil.Companion.standardized
import inas.anisha.srl_app.utils.CalendarUtil.Companion.toTimeString
import java.util.*

class ScheduleViewModel : ViewModel() {

    var id: Long = 0
    var type: Int = SkripsiConstant.SCHEDULE_TYPE_TASK
    var name: String = ""

    var startDate: Calendar = Calendar.getInstance().standardized()
    var endDate: Calendar = Calendar.getInstance().standardized()
    var executionTime: Calendar? = null

    var note: String = ""
    var priority: Int = 0
    var reward: String = ""
    var reminder: ReminderData? = null

    var isCompleted: Boolean = false
    var isOnTime: Boolean = false

    fun getDate(): String {
        var date = CalendarUtil.getDateDisplayDate(endDate) + "; "
        if (type != SkripsiConstant.SCHEDULE_TYPE_TASK) {
            date += startDate.toTimeString() + " - "
        }
        date += endDate.toTimeString()

        return date
    }

    fun getExecutionTimeDisplay(): String {
        executionTime?.let { return "Rencana pengerjaan: " + CalendarUtil.getDateDisplayDate(it) }
        return ""
    }

    fun getTypeText(): String {
        return when (type) {
            SkripsiConstant.SCHEDULE_TYPE_ACTIVITY -> "Kegiatan"
            SkripsiConstant.SCHEDULE_TYPE_TASK -> "Tugas"
            else -> "Ujian"
        }
    }

    fun getReminderText(): String {
        return if (reminder != null) "${reminder?.amount} ${SkripsiConstant.getScheduleReminderUnitString(
            reminder?.unit ?: 0
        )}" else ""
    }

    fun setReminderValue(reminder: ReminderData?) {
        this.reminder = if (reminder == null) null else
            reminder.addScheduleData(toEntity())
    }

    fun toEntity(): ScheduleEntity = ScheduleEntity(
        id,
        type,
        name,
        startDate.standardized(),
        endDate.standardized(),
        note,
        priority,
        reward,
        executionTime?.standardized(),
        isCompleted,
        isOnTime,
        reminder = if (reminder != null) GsonBuilder().create().toJson(reminder) else null
    )

    fun fromEntity(schedule: ScheduleEntity): ScheduleViewModel {
        id = schedule.id
        type = schedule.type
        name = schedule.name

        startDate = schedule.startDate.standardized()
        endDate = schedule.endDate.standardized()
        executionTime = schedule.executionTime?.standardized()

        note = schedule.note
        priority = schedule.priority
        reward = schedule.reward

        isCompleted = schedule.isCompleted
        isOnTime = schedule.isOnTime

        reminder = if (schedule.reminder == null) null else Gson().fromJson(
            schedule.reminder,
            ReminderData::class.java
        )

        return this
    }
}