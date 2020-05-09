package inas.anisha.srl_app.ui.main.schedule.displayday

import androidx.lifecycle.ViewModel
import inas.anisha.srl_app.R
import inas.anisha.srl_app.constant.SkripsiConstant

class ScheduleBlockViewModel : ViewModel() {
    var id: Long = 0
    var name: String = ""
    var type: Int = SkripsiConstant.SCHEDULE_TIMELINE_TYPE_CLASS
    var startMinute: Int = 0
    var endMinute: Int = 0
    var overlappingScheduleCount: Int = 0
    var exactScheduleCount: Int = 0
    var exactScheduleOrder: Int = 0

    var bgResource: Int = R.drawable.bg_schedule_class
    var height: Int = 0
    var width: Int = 0
    var marginStart: Int = 0
    var positionX: Int = 0
}