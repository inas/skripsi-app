package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class TargetUtamaViewModel : ViewModel() {
    var name: String = ""
    var note: String = ""
    var date: Calendar? = null
    var isEditable: Boolean = false
    var shouldShowSelection: Boolean = false
    var isSelected: MutableLiveData<Boolean> = MutableLiveData(false)

    fun dateString(): String {
        date?.let { return "" + it.get(Calendar.YEAR) }
        return ""
    }
}