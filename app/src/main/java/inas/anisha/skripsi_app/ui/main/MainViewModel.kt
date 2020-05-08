package inas.anisha.skripsi_app.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.ReminderEntity
import io.reactivex.Observable
import java.util.*

class MainViewModel(val mApplication: Application) : AndroidViewModel(mApplication) {
    var mRepository: Repository = Repository.getInstance(mApplication)

    fun getEvaluationDate() =
        Calendar.getInstance().apply { timeInMillis = mRepository.getEvaluationDate() }

    fun prepopulate() = mRepository.prepopulate()

    fun shouldShowSchoolScheduleDialog() = mRepository.shouldShowSchoolScheduleDialog()
    fun setShouldNotShowSchoolScheduleDialog() = mRepository.setShouldNotShowSchoolScheduleDialog()

    fun shouldShowEndOfCycleWarning() = mRepository.shouldShowEndOfCycleWarning()
    fun setShouldShowEndOfCycleWarning(shouldShow: Boolean) =
        mRepository.setShouldShowEndOfCycleWarning(shouldShow)

    fun shouldShowEvaluationReport() = mRepository.shouldShowEvaluationReport()

    fun scheduleReminders(reminders: List<ReminderEntity>) {
        val username = mRepository.getUserName()
        reminders.forEach { it.scheduleReminder(mApplication, username) }
    }

    fun getAllReminders(): Observable<List<ReminderEntity>> = mRepository.getAllReminders()

}