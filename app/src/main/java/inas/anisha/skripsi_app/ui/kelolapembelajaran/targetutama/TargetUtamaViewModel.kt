package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class TargetUtamaViewModel : ViewModel() {
    var name: String = ""
    var note: String = ""
    var date: Calendar? = null
    var isRemovable: Boolean = false
    var shouldShowSelection: Boolean = false
    var isSelected: MutableLiveData<Boolean> = MutableLiveData(false)

    fun dateString(): String {
        date?.let { return "" + it.get(Calendar.YEAR) }
        return ""
    }

    fun replaceValues(newTarget: TargetUtamaViewModel) {
        name = newTarget.name
        note = newTarget.note
        date = newTarget.date
        isRemovable = newTarget.isRemovable
        shouldShowSelection = newTarget.shouldShowSelection
        isSelected.value = newTarget.isSelected.value

    }
}