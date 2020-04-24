package inas.anisha.skripsi_app.ui.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.data.db.entity.SchoolClassEntity
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

        todaysClasses.forEach {
            viewModels.add(ScheduleTimelineViewModel().apply {
                name = it.name
                startMinute = it.startMinuteOfDay
                endMinute = it.endMinuteOfDay
                startTimeText = CalendarUtil.fromMinuteToTimeString(startMinute)
                endTimeText = CalendarUtil.fromMinuteToTimeString(endMinute)
            })
        }

        todaysActivities.forEach {
            viewModels.add(ScheduleTimelineViewModel().apply {
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
        viewModels.sortBy { it.startMinute }
        viewModels.forEachIndexed { index, it ->

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

        if (viewModels.isNotEmpty()) viewModels.last().isLastSchedule.value = true

        return viewModels
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
}