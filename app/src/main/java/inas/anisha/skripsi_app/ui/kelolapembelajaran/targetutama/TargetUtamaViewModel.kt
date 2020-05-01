package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity
import java.util.*

class TargetUtamaViewModel : ViewModel() {
    var name: String = ""
    var note: String = ""
    var date: Calendar? = null
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
    }

    fun toEntity(): TargetUtamaEntity = TargetUtamaEntity(0, name, note, date)

    fun fromEntity(target: TargetUtamaEntity): TargetUtamaViewModel {
        name = target.name
        note = target.note
        date = target.date
        return this
    }
}