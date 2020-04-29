package inas.anisha.skripsi_app.ui.main.schedule.school

import androidx.lifecycle.ViewModel
import inas.anisha.skripsi_app.data.db.entity.SchoolClassEntity
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.standardized
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toTimeString
import java.text.SimpleDateFormat
import java.util.*

class SchoolClassViewModel : ViewModel() {

    var id: Long = 0
    var name: String = ""
    var startTime: Calendar = Calendar.getInstance().standardized()
    var endTime: Calendar = Calendar.getInstance().standardized()
    var day: Int = 0
    var note: String = ""

    fun startTimeText() = startTime.toTimeString()
    fun endTimeText() = endTime.toTimeString()

    fun dayAndTimeText(): String {
        val dateFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dayString = dateFormat.format(
            Calendar.getInstance().apply { set(Calendar.DAY_OF_WEEK, day) }.timeInMillis
        )
        return dayString + ", " + startTimeText() + " - " + endTimeText()
    }

    fun fromEntity(schoolClass: SchoolClassEntity): SchoolClassViewModel {
        id = schoolClass.id
        name = schoolClass.name
        startTime = schoolClass.startTime
        endTime = schoolClass.endTime
        day = schoolClass.day
        note = schoolClass.note
        return this
    }

    fun toEntity(): SchoolClassEntity = SchoolClassEntity(id, name, startTime, endTime, day, note)
}