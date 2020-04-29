package inas.anisha.skripsi_app.ui.main.schedule.displayschool

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import inas.anisha.skripsi_app.ui.main.schedule.school.SchoolClassViewModel


class SchoolClassListPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val schoolClassFragments = mutableListOf(
        SchoolClassListFragment(),
        SchoolClassListFragment(),
        SchoolClassListFragment(),
        SchoolClassListFragment(),
        SchoolClassListFragment(),
        SchoolClassListFragment()
    )

    override fun getItem(position: Int): Fragment {
        return schoolClassFragments[position]
    }

    override fun getCount(): Int = NUM_ITEMS

    fun setItems(items: List<SchoolClassViewModel>, position: Int) {
        schoolClassFragments[position].setItems(items)
    }

    companion object {
        const val NUM_ITEMS = 6
    }
}