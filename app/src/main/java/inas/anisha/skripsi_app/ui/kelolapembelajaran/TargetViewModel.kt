package inas.anisha.skripsi_app.ui.kelolapembelajaran

import androidx.lifecycle.MutableLiveData
import java.util.*

interface TargetViewModel {
    var name: String
    var note: String
    var date: Calendar?
    var isEditable: Boolean
    var isSelected: MutableLiveData<Boolean>
    var shouldShowSelection: Boolean

    fun dateString(): String
}