package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
}