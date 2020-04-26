package inas.anisha.skripsi_app.ui.main.schedule.displaytask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentTaskListBinding

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
        adapter = TaskListAdapter().apply { setItems(taskListItems) }
        mBinding.recyclerView.adapter = adapter
    }

    fun setItems(items: List<TaskListItem>) {
        adapter?.setItems(items)
        taskListItems = items
    }
}