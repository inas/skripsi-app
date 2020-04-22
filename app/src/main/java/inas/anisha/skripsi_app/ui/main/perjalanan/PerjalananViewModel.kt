package inas.anisha.skripsi_app.ui.main.perjalanan

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.CycleEntity
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity

class PerjalananViewModel(application: Application) : AndroidViewModel(application) {

    val mRepository: Repository = Repository.getInstance(application)
    var cycleHistory: List<CycleEntity> = mutableListOf()

    fun getMainTarget(): LiveData<TargetUtamaEntity> = mRepository.getMainTarget()
    fun getSupportingTargets(): LiveData<List<TargetPendukungEntity>> =
        mRepository.getSupportingTargets()

    fun getUserName(): String = mRepository.getUserName()
    fun getUserGrade(): String = mRepository.getUserGrade()
    fun getUserStudy(): String = mRepository.getUserStudy()

    fun getAllCycle(): LiveData<List<CycleEntity>> = mRepository.getCycles()

    fun getCurrentCycleTasks(): LiveData<List<ScheduleEntity>> = mRepository.getCurrentCycleTasks()

}