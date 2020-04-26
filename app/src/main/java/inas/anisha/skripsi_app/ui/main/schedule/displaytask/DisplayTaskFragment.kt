package inas.anisha.skripsi_app.ui.main.schedule.displaytask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentDisplayTaskBinding
import inas.anisha.skripsi_app.ui.main.home.ImportantScheduleViewModel
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.standardized
import java.util.*

class DisplayTaskFragment : Fragment() {

    lateinit var mBinding: FragmentDisplayTaskBinding
    lateinit var mViewModel: DisplayTaskViewModel
    lateinit var mAdapter: TaskListPagerAdapter

    private var upcomingTasksObservable: LiveData<List<ImportantScheduleViewModel>>? = null
    private var pastTasksObservable: LiveData<List<ImportantScheduleViewModel>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(DisplayTaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_display_task, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = TaskListPagerAdapter(childFragmentManager)
        mBinding.viewPager.adapter = mAdapter
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)

        val currentDate = Calendar.getInstance().standardized()
        upcomingTasksObservable = mViewModel.getTasks(currentDate, true).apply {
            observe(this@DisplayTaskFragment, Observer { schedule ->
                mAdapter.setUpcomingTasks(constructTaskListItems(schedule))
            })
        }

        pastTasksObservable = mViewModel.getTasks(currentDate, false).apply {
            observe(this@DisplayTaskFragment, Observer { schedule ->
                mAdapter.setPastTasks(constructTaskListItems(schedule))
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        upcomingTasksObservable?.removeObservers(this)
        pastTasksObservable?.removeObservers(this)
    }

    fun constructTaskListItems(schedules: List<ImportantScheduleViewModel>): List<TaskListItem> {
        val groupedSchedule = schedules.groupBy { it.time }

        val taskListItems = mutableListOf<TaskListItem>()
        for ((date, tasks) in groupedSchedule) {
            taskListItems.add(TaskDateItem().apply { dateString = date })
            tasks.forEach { taskListItems.add(it) }
        }

        return taskListItems
    }

    companion object {
        const val TAG = "DISPLAY_TASK_FRAGMENT"
    }
}