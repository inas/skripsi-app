package inas.anisha.srl_app.ui.main.schedule.displaytask

interface TaskListItem {

    fun getType(): Int

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_CONTENT = 1
    }
}