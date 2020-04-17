package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TargetPendukungViewModel : ViewModel() {
    var name: String = ""
    var note: String = ""
    var time: String = ""
    var isRemovable: Boolean = false
    var shouldShowSelection: Boolean = false
    var isSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    var id: Long = 0

    fun shouldShowTime(): Boolean = time != ""

    fun replaceValues(newTarget: TargetPendukungViewModel) {
        name = newTarget.name
        note = newTarget.note
        time = newTarget.time
    }
}