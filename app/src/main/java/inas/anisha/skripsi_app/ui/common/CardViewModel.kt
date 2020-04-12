package inas.anisha.skripsi_app.ui.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class CardViewModel(application: Application) : AndroidViewModel(application) {
    var background: Int? = null
    var title: String = ""
    var description: String = ""
    var isVisible: Boolean = false
}