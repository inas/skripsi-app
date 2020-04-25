package inas.anisha.skripsi_app.ui.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.constant.SkripsiConstant.Companion.SCHEDULE_TIMELINE_TYPE_ACTIVITY
import inas.anisha.skripsi_app.constant.SkripsiConstant.Companion.SCHEDULE_TIMELINE_TYPE_CLASS
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.data.db.entity.SchoolClassEntity
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.main.schedule.ScheduleViewModel
import inas.anisha.skripsi_app.utils.CalendarUtil
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toMinuteOfDay
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toNextMidnight
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toPreviousMidnight
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toTimeString
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val mRepository: Repository = Repository.getInstance(application)
    val currentDate: MutableLiveData<Calendar> = MutableLiveData()
    var todaysClasses: List<SchoolClassEntity> = mutableListOf()
    var todaysActivities: List<ScheduleEntity> = mutableListOf()
    var todaysTasks: List<ScheduleEntity> = mutableListOf()
    var todaysTests: List<ScheduleEntity> = mutableListOf()

    var currentClassIndex = -1

    fun getTodaysClasses(): LiveData<List<SchoolClassEntity>> =
        Transformations.switchMap(currentDate) { today ->
            mRepository.getSchoolClassesSorted(today.get(Calendar.DAY_OF_WEEK))
        }

    fun getTomorrowsClassesCount(): LiveData<Int> =
        Transformations.switchMap(currentDate) { today ->
            mRepository.getSchoolCount(today.get(Calendar.DAY_OF_WEEK) + 1)
        }

    fun getTodaysSchedule(): LiveData<List<ScheduleEntity>> =
        Transformations.switchMap(currentDate) { today ->
            val prev = Calendar.getInstance().apply { time = today.time }
            val next = Calendar.getInstance().apply { time = today.time }
            mRepository.getScheduleSorted(prev.toPreviousMidnight(), next.toNextMidnight())
        }

    fun getTomorrowsSchedule(): LiveData<List<ScheduleEntity>> =
        Transformations.switchMap(currentDate) { today ->
            val tomorrow = today.apply { add(Calendar.DAY_OF_MONTH, 1) }
            val prev = Calendar.getInstance().apply { time = tomorrow.time }
            val next = Calendar.getInstance().apply { time = tomorrow.time }
            mRepository.getSchedule(prev.toPreviousMidnight(), next.toNextMidnight())
        }

    fun getCycleTasks(): LiveData<List<ScheduleEntity>> = mRepository.getCurrentCycleTasks()

    fun getTodaysScheduleViewModels(): List<ScheduleTimelineViewModel> {
        val viewModels = mutableListOf<ScheduleTimelineViewModel>()
        val classVms = mutableListOf<ScheduleTimelineViewModel>()
        val activityVms = mutableListOf<ScheduleTimelineViewModel>()

        todaysClasses.forEach {
            classVms.add(ScheduleTimelineViewModel().apply {
                id = it.id
                type = SCHEDULE_TIMELINE_TYPE_CLASS
                name = it.name
                startMinute = it.startMinuteOfDay
                endMinute = it.endMinuteOfDay
                startTimeText = CalendarUtil.fromMinuteToTimeString(startMinute)
                endTimeText = CalendarUtil.fromMinuteToTimeString(endMinute)
            })
        }

        todaysActivities.forEach {
            activityVms.add(ScheduleTimelineViewModel().apply {
                id = it.id
                type = SCHEDULE_TIMELINE_TYPE_ACTIVITY
                name = it.name
                startMinute = it.startMinuteOfDay
                endMinute = it.endMinuteOfDay
                startTimeText = CalendarUtil.fromMinuteToTimeString(startMinute)
                endTimeText = CalendarUtil.fromMinuteToTimeString(endMinute)
            })
        }

        val currentMinuteOfDay = currentDate.value?.toMinuteOfDay() ?: 0

        currentClassIndex = -1
        var previousSchedule: ScheduleTimelineViewModel? = null
        var toBeRemovedSchedule: MutableList<ScheduleTimelineViewModel> = mutableListOf()
//        val sortedVms =
//            viewModels.sortedWith(compareBy<ScheduleTimelineViewModel> { it.startMinute }.thenBy { it.endMinute })
        val sortedVms = mergeList(classVms, activityVms)
        sortedVms.forEachIndexed { index, it ->

            previousSchedule?.let { prev ->
                if (prev.endMinute == it.startMinute) {
                    prev.hasImmediateSchedule.value = true
                    it.isStartIncludeCurrentSchedule.value =
                        prev.isStartIncludeCurrentSchedule.value
                }
            }

            if (it.startMinute <= currentMinuteOfDay && currentMinuteOfDay <= it.endMinute) {
                it.isStartIncludeCurrentSchedule.value = true
                it.isEndIncludeCurrentSchedule.value = true
                currentClassIndex = index
            }

            previousSchedule = it
        }

        if (sortedVms.isNotEmpty()) sortedVms.last().isLastSchedule.value = true

        return sortedVms
    }

    fun mergeList(
        classVms: List<ScheduleTimelineViewModel>,
        activityVms: List<ScheduleTimelineViewModel>
    ): List<ScheduleTimelineViewModel> {
        val n1 = classVms.size
        val n2 = activityVms.size

        var i = 0
        var j = 0
        var k = 0

        val sortedList = mutableListOf<ScheduleTimelineViewModel>()

        var prev: ScheduleTimelineViewModel? = null
        while (i < n1 && j < n2) {
            if (classVms[i].startMinute < activityVms[j].startMinute) {
                val toBeAdded = classVms[i++]
                if (prev != null && toBeAdded.startMinute < prev.endMinute) {
                    sortedList[k - 1] = toBeAdded
                } else {
                    sortedList.add(k++, toBeAdded)
                }
            } else if (classVms[i].startMinute > activityVms[j].startMinute) {
                val toBeAdded = activityVms[j++]
                if (prev == null || toBeAdded.startMinute >= prev.endMinute) {
                    sortedList.add(k++, toBeAdded)
                }
            } else {
                j++
                if (prev != null && classVms[i].startMinute < prev.endMinute) {
                    sortedList[k - 1] = classVms[i++]
                } else {
                    sortedList.add(k++, classVms[i++])
                }
            }
            prev = sortedList[k - 1]
        }

        if (sortedList.isEmpty()) {
            while (i < n1) sortedList.add(k++, classVms[i++])
            while (j < n2) sortedList.add(k++, activityVms[j++])
        } else {
            val lastElement = sortedList.last()
            if (i < n1) { // only class vms left
                if (lastElement.type == SCHEDULE_TIMELINE_TYPE_ACTIVITY && classVms[i].startMinute < lastElement.endMinute) {
                    sortedList[k - 1] = classVms[i++]
                }
                if (i < n1) while (i < n1) sortedList.add(k++, classVms[i++])
            } else {
                while (j < n2) {
                    val toBeAdded = activityVms[j++]
                    if (toBeAdded.startMinute >= lastElement.endMinute) {
                        sortedList.add(k++, toBeAdded)
                    }
                }
            }

        }

        return sortedList
    }

    fun getImportantScheduleViewModels(schedules: List<ScheduleEntity>): List<ImportantScheduleViewModel> {
        val viewModels = mutableListOf<ImportantScheduleViewModel>()

        schedules.forEach {
            viewModels.add(ImportantScheduleViewModel().apply {
                id = it.id
                name = it.name
                time = it.startDate.toTimeString()
                if (it.type != SkripsiConstant.SCHEDULE_TYPE_TASK) time += " - " + it.endDate.toTimeString()
                rating = it.priority
                isCompleted = it.isCompleted
            })
        }

        return viewModels
    }

    fun addSupportingTarget(target: TargetPendukungViewModel) =
        mRepository.addSupportingTarget(target.toEntity())

    fun addSchedule(schedule: ScheduleViewModel) = mRepository.addSchedule(schedule.toEntity())
}