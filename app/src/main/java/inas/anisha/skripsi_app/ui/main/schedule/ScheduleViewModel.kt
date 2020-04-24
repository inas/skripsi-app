package inas.anisha.skripsi_app.ui.main.schedule

import androidx.lifecycle.ViewModel
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.utils.CalendarUtil
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.standardize
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toTimeString
import java.util.*

class ScheduleViewModel : ViewModel() {

    var id: Long = 0
    var type: Int = 0
    var name: String = ""

    var startDate: Calendar = Calendar.getInstance().standardize()
    var endDate: Calendar = Calendar.getInstance().standardize()
    var executionTime: Calendar? = null

    var note: String = ""
    var priority: Int = 0
    var reward: String = ""

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

    fun toEntity(): ScheduleEntity = ScheduleEntity(
        id,
        type,
        name,
        startDate.standardize(),
        endDate.standardize(),
        note,
        priority,
        reward,
        executionTime?.standardize(),
        isCompleted,
        isOnTime
    )

    fun fromEntity(schedule: ScheduleEntity): ScheduleViewModel {
        id = schedule.id
        type = schedule.type
        name = schedule.name

        startDate = schedule.startDate.standardize()
        endDate = schedule.endDate.standardize()
        executionTime = schedule.executionTime?.standardize()

        note = schedule.note
        priority = schedule.priority
        reward = schedule.reward

        isCompleted = schedule.isCompleted
        isOnTime = schedule.isOnTime

        return this
    }
}