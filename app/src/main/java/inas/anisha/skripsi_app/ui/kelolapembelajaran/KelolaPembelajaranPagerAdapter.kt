package inas.anisha.skripsi_app.ui.kelolapembelajaran

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import inas.anisha.skripsi_app.R


class KelolaPembelajaranPagerAdapter(
    fragmentManager: FragmentManager,
    val listener: OnboardingPagerFragment.ButtonClickListener
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingPagerFragment.newInstance(
                "Raih Target Belajar",
                "Tetapkan target lalu kelola jadwal dan strategi belajarmu",
                0,
                R.drawable.img_onboarding_0,
                object : OnboardingPagerFragment.ButtonClickListener {
                    override fun onButtonClick(buttonType: Int) {
                        listener.onButtonClick(buttonType)
                    }
                })

            1 -> OnboardingPagerFragment.newInstance(
                "Amati Progres Belajar",
                "Pantau dan tingkatkan perkembangan belajarmu",
                1,
                R.drawable.img_onboarding_1,
                object : OnboardingPagerFragment.ButtonClickListener {
                    override fun onButtonClick(buttonType: Int) {
                        listener.onButtonClick(buttonType)
                    }
                })

            else -> OnboardingPagerFragment.newInstance(
                "Evaluasi Pembelajaran",
                "Evaluasi pada akhir siklus belajarmu dan perbaiki rencana kedepannya",
                2,
                R.drawable.img_onboarding_2,
                object : OnboardingPagerFragment.ButtonClickListener {
                    override fun onButtonClick(buttonType: Int) {
                        listener.onButtonClick(buttonType)
                    }
                })
        }
    }

    override fun getCount(): Int = NUM_ITEMS

    companion object {
        private const val NUM_ITEMS = 3
    }
}