package inas.anisha.skripsi_app.ui.main.schedule

import androidx.lifecycle.ViewModel
import inas.anisha.skripsi_app.data.db.entity.SchoolClassEntity
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.standardized
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toTimeString
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

    fun fromEntity(schoolClass: SchoolClassEntity) {
        id = schoolClass.id
        name = schoolClass.name
        startTime = schoolClass.startTime
        endTime = schoolClass.endTime
        day = schoolClass.day
        note = schoolClass.note
    }

    fun toEntity(): SchoolClassEntity = SchoolClassEntity(id, name, startTime, endTime, day, note)
}