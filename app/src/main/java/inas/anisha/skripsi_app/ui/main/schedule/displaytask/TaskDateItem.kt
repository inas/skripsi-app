package inas.anisha.skripsi_app.ui.main.schedule.displaytask

class TaskDateItem : TaskListItem {

    var dateString: String = ""

    override fun getType(): Int {
        return TaskListItem.TYPE_HEADER
    }
}