package inas.anisha.skripsi_app.ui.main.schedule.displaytask

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class TaskListPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mUpcomingTaskFragment = TaskListFragment()
    private val mPastTaskFragment = TaskListFragment()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> mUpcomingTaskFragment
            else -> mPastTaskFragment
        }
    }

    override fun getCount(): Int = NUM_ITEMS

    fun setUpcomingTasks(tasks: List<TaskListItem>) {
        mUpcomingTaskFragment.setItems(tasks)
    }

    fun setPastTasks(tasks: List<TaskListItem>) {
        mPastTaskFragment.setItems(tasks)
    }

    companion object {
        const val NUM_ITEMS = 6
    }
}