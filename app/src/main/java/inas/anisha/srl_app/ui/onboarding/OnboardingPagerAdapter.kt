package inas.anisha.srl_app.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import inas.anisha.srl_app.R


class OnboardingPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingPagerFragment.newInstance(
                "Raih Target Belajar",
                "Tetapkan target lalu kelola jadwal dan strategi belajarmu",
                R.drawable.img_onboarding_0
            )

            1 -> OnboardingPagerFragment.newInstance(
                "Amati Progres Belajar",
                "Pantau dan tingkatkan perkembangan belajarmu",
                R.drawable.img_onboarding_1
            )

            else -> OnboardingPagerFragment.newInstance(
                "Evaluasi Pembelajaran",
                "Evaluasi pada akhir siklus belajarmu dan perbaiki rencana kedepannya",
                R.drawable.img_onboarding_2
            )
        }
    }

    override fun getCount(): Int = NUM_ITEMS

    companion object {
        private const val NUM_ITEMS = 3
    }
}