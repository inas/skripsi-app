package inas.anisha.skripsi_app.ui.main.perjalanan

import androidx.lifecycle.ViewModel
import inas.anisha.skripsi_app.data.db.entity.CycleEntity
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toDateString
import java.util.*

class CycleViewModel : ViewModel() {
    var number: Int = 0
    var completion: Int = 0
    var reflection: String = ""
    var startDate: Calendar = Calendar.getInstance()
    var endDate: Calendar = Calendar.getInstance()
    var totalTask: Int = 0
    var completedTask: Int = 0
    var onTimeTask: Int = 0
    var totalTarget: Int = 0
    var completedTarget: Int = 0
    var completedTargetList: List<String> = mutableListOf()
    var frequency: Int = 0
    var duration: Int = 0

    fun getCycleName(): String = "Siklus $number"
    fun getCyclePeriod(): String = startDate.toDateString() + " - " + endDate.toDateString()

    fun getCompletedTaskString(): String = "$completedTask/$totalTask"

    fun getOnTimeTaskString(): String {
        return if (totalTask == 0) "0%"
        else {
            val percentage = onTimeTask * 100 / totalTask
            "$percentage%"
        }
    }

    fun getCompletedTargetString(): String {
        return if (totalTask == 0) "0%"
        else {
            val percentage = completedTarget * 100 / totalTarget
            "$percentage%"
        }
    }

    fun fromEntity(cycle: CycleEntity) {
        number = cycle.number
        completion = cycle.completion
        reflection = cycle.reflection
        startDate = cycle.startDate
        endDate = cycle.endDate
        totalTarget = cycle.totalTarget
        totalTask = cycle.totalTask
        completedTask = cycle.completedTask
        completedTarget = cycle.completedTarget
        onTimeTask = cycle.onTimeTask
        totalTarget = cycle.totalTarget
        completedTarget = cycle.completedTarget
        completedTargetList = cycle.completedTargetList
        frequency = cycle.frequency
        duration = cycle.duration
    }
}