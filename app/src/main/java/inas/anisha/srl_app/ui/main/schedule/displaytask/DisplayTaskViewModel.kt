package inas.anisha.srl_app.ui.main.schedule.displaytask

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import inas.anisha.srl_app.data.Repository
import inas.anisha.srl_app.ui.main.home.ImportantScheduleViewModel
import inas.anisha.srl_app.utils.CalendarUtil.Companion.toTimeString
import java.util.*

class DisplayTaskViewModel(application: Application) : AndroidViewModel(application) {

    val mRepository: Repository = Repository.getInstance(application)

    fun getTasks(
        dateLimit: Calendar,
        isUpcomingTasks: Boolean
    ): LiveData<List<ImportantScheduleViewModel>> {
        return Transformations.map(mRepository.getTasks(dateLimit, isUpcomingTasks)) { data ->
            data.map {
                ImportantScheduleViewModel().apply {
                    id = it.id
                    name = it.name
                    timeMillis = it.startDate.timeInMillis
                    time = it.startDate.toTimeString()
                    rating = it.priority
                    isCompleted = it.isCompleted
                }
            }
        }
    }
}