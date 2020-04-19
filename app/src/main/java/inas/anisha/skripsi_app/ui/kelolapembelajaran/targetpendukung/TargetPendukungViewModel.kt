package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity

class TargetPendukungViewModel : ViewModel() {
    var id: Long = 0
    var name: String = ""
    var note: String = ""
    var time: String = ""
    var isCompleted: Boolean = false
    var isSelected: MutableLiveData<Boolean> = MutableLiveData(false)

    fun shouldShowTime(): Boolean = time != ""

    fun replaceValues(newTarget: TargetPendukungViewModel) {
        name = newTarget.name
        note = newTarget.note
        time = newTarget.time
    }

    fun fromEntity(targetEntity: TargetPendukungEntity) {
        id = targetEntity.id
        name = targetEntity.name
        note = targetEntity.note
        time = targetEntity.time
        isCompleted = targetEntity.isCompleted
    }

    fun toEntity(): TargetPendukungEntity = TargetPendukungEntity(id, name, note, time, isCompleted)
}