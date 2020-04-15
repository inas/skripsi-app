package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.TargetViewModel
import java.util.*

class TargetPendukungViewModel : ViewModel(),
    TargetViewModel {
    override var name: String = ""
    override var note: String = ""
    override var date: Calendar? = null
    override var isEditable: Boolean = false
    override var shouldShowSelection: Boolean = false
    override var isSelected: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun dateString(): String {
        date?.let { return "" + it.get(Calendar.YEAR) }
        return ""
    }
}