package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class TargetUtamaViewModel(application: Application) : AndroidViewModel(application) {

    var isTargetAdded: Boolean = false
    var addedTargetIsSelected: Boolean = false
    var recTarget0IsSelected: Boolean = false
    var recTarget1IsSelected: Boolean = false

    fun selectarget(addedTarget: Boolean, firstRecTarget: Boolean, secondRecTarget: Boolean) {
        addedTargetIsSelected = addedTarget
        recTarget0IsSelected = firstRecTarget
        recTarget1IsSelected = secondRecTarget
    }
}