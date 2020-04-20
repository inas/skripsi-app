package inas.anisha.skripsi_app.ui.main.target

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel

class TargetViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository = Repository.getInstance(application)
    var mainTarget = TargetUtamaViewModel()

    fun getMainTarget(): LiveData<TargetUtamaEntity> = mRepository.getMainTarget()

    fun updateMainTarget(target: TargetUtamaViewModel) {
        mRepository.setMainTarget(TargetUtamaEntity(0, target.name, target.note, target.date))
    }

    fun getMainTargetViewModel(target: TargetUtamaEntity): TargetUtamaViewModel {
        mainTarget = TargetUtamaViewModel().apply {
            name = target.name
            note = target.note
            date = target.date
            shouldShowSelection = false
        }

        return mainTarget
    }

    fun getCycleTime(): Pair<Int, Int> = mRepository.getCycleTime()

    fun getCompletedSupportingTargets(): LiveData<List<TargetPendukungViewModel>> {
        return Transformations.map(mRepository.getSupportingTargetsByCompleteness(true)) { data ->
            data.map {
                TargetPendukungViewModel().apply { fromEntity(it) }
            }
        }
    }

    fun getIncompleteSupportingTargets(): LiveData<List<TargetPendukungViewModel>> {
        return Transformations.map(mRepository.getSupportingTargetsByCompleteness(false)) { data ->
            data.map {
                TargetPendukungViewModel().apply { fromEntity(it) }
            }
        }
    }

    fun addOrUpdateSupportingTarget(target: TargetPendukungViewModel) =
        mRepository.addSupportingTarget(target.toEntity())

    fun deleteSupportingTarget(targetId: Long) = mRepository.deleteSupportingTargets(targetId)

    fun getEvaluationDate(): Long = mRepository.getEvaluationDate()
}