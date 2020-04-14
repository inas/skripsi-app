package inas.anisha.skripsi_app.ui.common.addTarget

import androidx.lifecycle.MutableLiveData
import java.util.*

interface TargetViewModel {
    var name: String
    var note: String
    var date: Calendar?
    var isEditable: Boolean
    var isSelected: MutableLiveData<Boolean>

    fun dateString(): String
}