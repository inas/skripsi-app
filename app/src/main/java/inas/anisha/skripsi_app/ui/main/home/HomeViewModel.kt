package inas.anisha.skripsi_app.ui.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.data.db.entity.SchoolClassEntity
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toNextMidnight
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toPreviousMidnight
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val mRepository: Repository = Repository.getInstance(application)
    val currentDate: MutableLiveData<Calendar> = MutableLiveData()
    var todaysClasses: List<SchoolClassEntity> = mutableListOf()
    var todaysActivities: List<ScheduleEntity> = mutableListOf()
    var todaysTasks: List<ScheduleEntity> = mutableListOf()
    var todaysTests: List<ScheduleEntity> = mutableListOf()

    fun setCurrentDate(date: Calendar) {
        currentDate.value = date
    }

    fun getTodaysClasses(): LiveData<List<SchoolClassEntity>> =
        Transformations.switchMap(currentDate) { today ->
            mRepository.getSchoolClasses(today.get(Calendar.DAY_OF_WEEK))
        }

    fun getTomorrowsClassesCount(): LiveData<Int> =
        Transformations.switchMap(currentDate) { today ->
            mRepository.getSchoolCount(today.get(Calendar.DAY_OF_WEEK) + 1)
        }

    fun getTodaysSchedule(): LiveData<List<ScheduleEntity>> =
        Transformations.switchMap(currentDate) { today ->
            mRepository.getSchedule(today.toPreviousMidnight(), today.toNextMidnight())
        }

    fun getTomorrowsSchedule(): LiveData<List<ScheduleEntity>> =
        Transformations.switchMap(currentDate) { today ->
            val tomorrow = today.apply { add(Calendar.DAY_OF_MONTH, 1) }
            mRepository.getSchedule(tomorrow.toPreviousMidnight(), tomorrow.toNextMidnight())
        }

    fun getCycleTasks(): LiveData<List<ScheduleEntity>> = mRepository.getCurrentCycleTasks()

    fun getTodaysScheduleViewModels(): List<ScheduleTimelineViewModel> {
        val viewModels = mutableListOf<ScheduleTimelineViewModel>()

        todaysClasses.forEach { }

        todaysActivities.forEach { }

        return viewModels
    }

    fun getImportantScheduleViewModels(schedules: List<ScheduleEntity>): List<ImportantScheduleViewModel> {
        val viewModels = mutableListOf<ImportantScheduleViewModel>()

        schedules.forEach { }

        return viewModels
    }
}