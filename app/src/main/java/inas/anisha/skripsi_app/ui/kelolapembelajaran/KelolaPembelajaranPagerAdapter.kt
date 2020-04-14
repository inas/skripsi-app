package inas.anisha.skripsi_app.ui.kelolapembelajaran

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import inas.anisha.skripsi_app.ui.kelolapembelajaran.siklusbelajar.SiklusBelajarFragment
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaFragment


class KelolaPembelajaranPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> TargetUtamaFragment()
            2 -> SiklusBelajarFragment()
            else -> TargetUtamaFragment()
        }
    }

    override fun getCount(): Int = NUM_ITEMS

    companion object {
        const val NUM_ITEMS = 3
    }
}