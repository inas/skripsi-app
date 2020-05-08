package inas.anisha.skripsi_app.ui.main.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.main.schedule.schedule.ScheduleViewModel
import inas.anisha.skripsi_app.ui.main.schedule.school.SchoolClassViewModel

class SchedulePageViewModel(val mApplication: Application) : AndroidViewModel(mApplication) {

    val mRepository: Repository = Repository.getInstance(mApplication)

    fun addSupportingTarget(target: TargetPendukungViewModel) =
        mRepository.addSupportingTarget(target.toEntity())

    fun addSchedule(schedule: ScheduleViewModel) {
        mRepository.addSchedule(schedule.toEntity(), schedule.reminder)
    }

    fun addSchoolClass(schoolClass: SchoolClassViewModel) =
        mRepository.addSchoolClass(schoolClass.toEntity())
}