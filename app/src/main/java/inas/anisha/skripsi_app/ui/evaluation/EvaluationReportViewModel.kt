package inas.anisha.skripsi_app.ui.evaluation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.CycleEntity
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import io.reactivex.Observable
import java.util.*

class EvaluationReportViewModel(application: Application) : AndroidViewModel(application) {
    val mRepository: Repository = Repository.getInstance(application)

    var id: Long = 0
    var cycleNumber: Int = 0
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

    var taskNames: MutableList<String> = mutableListOf()
    var taskCompletionIcons: MutableList<Int> = mutableListOf()
    var taskOnTimeIcons: MutableList<Int> = mutableListOf()
    var targetNames: MutableList<String> = mutableListOf()
    var targetIcons: MutableList<Int> = mutableListOf()

    fun getCycleStartDate(): Calendar {
        startDate = Calendar.getInstance().apply { timeInMillis = mRepository.getCycleStartDate() }
        return startDate
    }

    fun getCycleEvaluationDate(): Calendar {
        endDate = Calendar.getInstance().apply {
            timeInMillis = mRepository.getEvaluationDate()
            add(Calendar.DAY_OF_MONTH, -1)
        }
        return endDate
    }

    fun getCurrentCycle(): Observable<CycleEntity> = mRepository.getCurrentCycle()
    fun getCycleTime(): String {
        val cycleTime = mRepository.getCycleTime()
        frequency = cycleTime.first
        duration = cycleTime.second

        val frequencyString = when (frequency) {
            SkripsiConstant.CYCLE_FREQUENCY_DAILY -> "Harian"
            SkripsiConstant.CYCLE_FREQUENCY_WEEKLY -> "Mingguan"
            else -> "Bulanan"
        }

        val durationString = if (cycleTime.second == 1) "" else ("" + cycleTime.second + " ")
        return durationString + frequencyString
    }

    fun getSupportingTargets(): LiveData<List<TargetPendukungEntity>> =
        mRepository.getSupportingTargets()

    fun getCurrentCycleTasks(): LiveData<List<ScheduleEntity>> = mRepository.getCurrentCycleTasks()

    fun processTasks(tasks: List<ScheduleEntity>) {
        var completedTaskCount = 0
        var onTimeTaskCount = 0

        val names = mutableListOf<String>()
        val completionIcons = mutableListOf<Int>()
        val onTimeIcons = mutableListOf<Int>()

        tasks.forEach {
            names.add(it.name)

            if (it.isCompleted) {
                completionIcons.add(R.drawable.ic_check_green_white)
                completedTaskCount++
            } else {
                completionIcons.add(R.drawable.ic_cross)
            }

            if (it.isOnTime) {
                onTimeIcons.add(R.drawable.ic_check_green_white)
                onTimeTaskCount++
            } else {
                onTimeIcons.add(R.drawable.ic_cross)
            }
        }

        taskNames = names
        taskCompletionIcons = completionIcons
        taskOnTimeIcons = onTimeIcons

        totalTask = tasks.size
        completedTask = completedTaskCount
        onTimeTask = onTimeTaskCount
    }

    fun processTargets(targets: List<TargetPendukungEntity>) {
        var completedTargetCount = 0
        val statusPair = mutableListOf<Pair<String, Int>>()

        val completedNames = mutableListOf<String>()
        val names = mutableListOf<String>()
        val icList = mutableListOf<Int>()
        targets.forEach {
            names.add(it.name)
            if (it.isCompleted) {
                statusPair.add(Pair(it.name, R.drawable.ic_check_green_white))
                icList.add(R.drawable.ic_check_green_white)
                completedTargetCount++
                completedNames.add(it.name)
            } else {
                statusPair.add(Pair(it.name, R.drawable.ic_cross))
                icList.add(R.drawable.ic_cross)
            }
        }

        totalTarget = targets.size
        completedTarget = completedTargetCount
        completedTargetList = completedNames
        targetNames = names
        targetIcons = icList
    }

    fun getCompletedTaskString(): String = "" + completedTask + "/" + totalTask + " tugas selesai"
    fun getOnTimeTaskString(): String =
        "" + onTimeTask + "/" + totalTask + " tugas dikerjakan tepat waktu"

    fun getCompletedTargetString(): String =
        "" + completedTarget + "/" + totalTarget + " target tercapai"

    fun saveCycle(reflection: String) {
        completion = if (targetNames.isNotEmpty()) {
            completedTarget * 100 / totalTarget
        } else {
            0
        }
        val cycleEntity = CycleEntity(
            id,
            cycleNumber,
            completion,
            reflection,
            startDate,
            endDate,
            totalTask,
            completedTask,
            onTimeTask,
            totalTarget,
            completedTarget,
            completedTargetList,
            frequency,
            duration
        )

        mRepository.updateCycle(cycleEntity)
    }
}