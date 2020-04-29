package inas.anisha.skripsi_app.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var mRepository: Repository = Repository.getInstance(application)

    fun getEvaluationDate() =
        Calendar.getInstance().apply { timeInMillis = mRepository.getEvaluationDate() }

    fun prepopulate() = mRepository.prepopulate()

    fun shouldShowSchoolScheduleDialog() = mRepository.shouldShowSchoolScheduleDialog()
    fun setShouldNotShowSchoolScheduleDialog() = mRepository.setShouldNotShowSchoolScheduleDialog()

    fun shouldShowEndOfCycleWarning() = mRepository.shouldShowEndOfCycleWarning()
    fun setShouldShowEndOfCycleWarning(shouldShow: Boolean) =
        mRepository.setShouldShowEndOfCycleWarning(shouldShow)

    fun shouldShowEvaluationReport() = mRepository.shouldShowEvaluationReport()
    fun setShouldShowEvaluationReport(shouldShow: Boolean) =
        mRepository.setShouldShowEvaluationReport(shouldShow)

}