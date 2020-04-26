package inas.anisha.skripsi_app.ui.main.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel

class SchedulePageViewModel(application: Application) : AndroidViewModel(application) {

    val mRepository: Repository = Repository.getInstance(application)

    fun addSupportingTarget(target: TargetPendukungViewModel) =
        mRepository.addSupportingTarget(target.toEntity())

    fun addSchedule(schedule: ScheduleViewModel) = mRepository.addSchedule(schedule.toEntity())
    fun addSchoolClass(schoolClass: SchoolClassViewModel) =
        mRepository.addSchoolClass(schoolClass.toEntity())
}