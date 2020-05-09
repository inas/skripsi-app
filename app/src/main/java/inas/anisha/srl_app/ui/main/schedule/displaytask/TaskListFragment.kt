package inas.anisha.srl_app.ui.main.schedule.displaytask

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.FragmentTaskListBinding
import inas.anisha.srl_app.ui.main.schedule.schedule.ScheduleDetailActivity

class TaskListFragment : Fragment() {

    lateinit var mBinding: FragmentTaskListBinding

    private var adapter: TaskListAdapter? = null
    private var taskListItems: List<TaskListItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task_list, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TaskListAdapter().apply {
            setItems(taskListItems)
            setListener(object : TaskListAdapter.ItemClickListener {
                override fun onClick(itemId: Long) {
                    openScheduleDetail(itemId)
                }
            })
        }

        mBinding.recyclerView.adapter = adapter
        mBinding.textviewEmpty.visibility = if (taskListItems.isEmpty()) View.VISIBLE else View.GONE
    }

    fun setItems(items: List<TaskListItem>) {
        if (::mBinding.isInitialized) {
            mBinding.textviewEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }
        adapter?.setItems(items)
        taskListItems = items
    }

    fun openScheduleDetail(scheduleId: Long) {
        val intent = Intent(activity, ScheduleDetailActivity::class.java).apply {
            putExtra(
                ScheduleDetailActivity.EXTRA_ID,
                scheduleId
            )
        }
        startActivity(intent)
    }
}