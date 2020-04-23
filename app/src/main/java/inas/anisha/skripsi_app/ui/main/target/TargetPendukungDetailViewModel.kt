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

    fun getSupportingTarget(targetId: Long): LiveData<TargetPendukungEntity> =
        mRepository.getSupportingTarget(targetId)

    fun getSupportingTargetViewModel(target: TargetPendukungEntity): TargetPendukungViewModel =
        TargetPendukungViewModel().fromEntity(target)

    fun deleteSupportingTarget() = mRepository.deleteSupportingTargets(target.id)

    fun updateSupportingTarget(target: TargetPendukungViewModel) =
        mRepository.updateSupportingTarget(target.toEntity())

    fun setTargetAsComplete(isCompleted: Boolean) = mRepository.updateSupportingTarget(
        TargetPendukungEntity(
            target.id, target.name,
            target.note,
            target.time,
            isCompleted
        )
    )

}