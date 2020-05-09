package inas.anisha.srl_app.ui.main.home

import androidx.lifecycle.ViewModel
import inas.anisha.srl_app.ui.main.schedule.displaytask.TaskListItem

class ImportantScheduleViewModel : ViewModel(), TaskListItem {
    var id: Long = 0
    var name: String = ""
    var time: String = ""
    var timeMillis: Long = 0
    var rating: Int = 0
    var isCompleted: Boolean = false

    override fun getType(): Int {
        return TaskListItem.TYPE_CONTENT
    }
}
