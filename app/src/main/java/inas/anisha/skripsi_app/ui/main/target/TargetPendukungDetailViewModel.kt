package inas.anisha.skripsi_app.ui.main.target

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel

class TargetPendukungDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val mRepository = Repository.getInstance(application)
    var target: TargetPendukungEntity = TargetPendukungEntity(0, "", "", "")

    fun getSupportingTargetViewModel(target: TargetPendukungEntity): TargetPendukungViewModel {
        return TargetPendukungViewModel().apply {
            name = target.name
            note = target.note
            time = target.time
            isCompleted = target.isCompleted
        }
    }

    fun getSupportingTarget(targetId: Long): LiveData<TargetPendukungEntity> =
        mRepository.getSupportingTarget(targetId)

    fun setTargetAsComplete(isCompleted: Boolean) = mRepository.updateSupportingTarget(
        target.id,
        target.name,
        target.note,
        target.time,
        isCompleted
    )

    fun deleteSupportingTarget() = mRepository.deleteSupportingTargets(target.id)

}