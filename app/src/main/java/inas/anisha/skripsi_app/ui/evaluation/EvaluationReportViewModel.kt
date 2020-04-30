package inas.anisha.skripsi_app.ui.evaluation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.CycleEntity
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import io.reactivex.Observable

class EvaluationReportViewModel(application: Application) : AndroidViewModel(application) {
    val mRepository: Repository = Repository.getInstance(application)

    fun getCycleStartDate() = mRepository.getCycleStartDate()
    fun getCycleEndDate() = mRepository.getEvaluationDate()

    fun getCurrentCycle(): Observable<CycleEntity> = mRepository.getCurrentCycle()
    fun getCycleTime(): Pair<Int, Int> = mRepository.getCycleTime()

    fun getSupportingTargets(): LiveData<List<TargetPendukungEntity>> =
        mRepository.getSupportingTargets()
}