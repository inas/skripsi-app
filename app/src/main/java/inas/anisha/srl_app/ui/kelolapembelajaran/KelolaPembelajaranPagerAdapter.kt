package inas.anisha.srl_app.ui.kelolapembelajaran

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import inas.anisha.srl_app.ui.kelolapembelajaran.siklusbelajar.SiklusBelajarFragment
import inas.anisha.srl_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungFragment
import inas.anisha.srl_app.ui.kelolapembelajaran.targetutama.TargetUtamaFragment


class KelolaPembelajaranPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val mTargetUtamaFragment = TargetUtamaFragment()
    private val mSiklusBelajarFragment = SiklusBelajarFragment()
    private val mTargetPendukungFragment = TargetPendukungFragment()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> mTargetUtamaFragment
            1 -> mSiklusBelajarFragment
            else -> mTargetPendukungFragment
        }
    }

    override fun getCount(): Int = NUM_ITEMS

    fun completeKelolaPembelajaran() {
        mTargetPendukungFragment.getSelectedTargets()
    }

    companion object {
        const val NUM_ITEMS = 3
    }
}