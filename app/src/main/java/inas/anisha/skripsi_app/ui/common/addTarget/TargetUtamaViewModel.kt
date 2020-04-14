package inas.anisha.skripsi_app.ui.common.addTarget

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class TargetUtamaViewModel : ViewModel(), TargetViewModel {
    override var name: String = ""
    override var note: String = ""
    override var date: Calendar? = null
    override var isEditable: Boolean = false
    override var isSelected: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun dateString(): String {
        date?.let { return "" + it.get(Calendar.YEAR) }
        return ""
    }
}