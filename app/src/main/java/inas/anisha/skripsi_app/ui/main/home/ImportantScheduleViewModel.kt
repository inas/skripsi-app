package inas.anisha.skripsi_app.ui.main.home

import androidx.lifecycle.ViewModel

class ImportantScheduleViewModel : ViewModel() {
    var id: Long = 0
    var name: String = ""
    var time: String = ""
    var rating: Int = 0
    var isCompleted: Boolean = false
}
