package inas.anisha.skripsi_app.ui.main.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity

class ScheduleDetailViewModel(application: Application) : AndroidViewModel(application) {

    val mRepository = Repository.getInstance(application)
    var schedule: ScheduleEntity = ScheduleEntity(0)

    fun getSchedule(scheduleId: Long): LiveData<ScheduleEntity> =
        mRepository.getSchedule(scheduleId)

    fun updateScheduleAsComplete(isComplete: Boolean) =
        mRepository.updateScheduleAsComplete(schedule.id, isComplete)

}