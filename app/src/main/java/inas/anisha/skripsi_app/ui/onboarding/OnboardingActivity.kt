package inas.anisha.skripsi_app.ui.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ActivityOnboardingBinding
import inas.anisha.skripsi_app.ui.onboarding.OnboardingPagerFragment.Companion.CLICK_NEXT
import inas.anisha.skripsi_app.ui.onboarding.OnboardingPagerFragment.Companion.CLICK_SKIP
import inas.anisha.skripsi_app.ui.onboarding.OnboardingPagerFragment.Companion.CLICK_START

class OnboardingActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityOnboardingBinding
    private lateinit var mViewModel: OnboardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
        mViewModel = ViewModelProviders.of(this).get(OnboardingViewModel::class.java)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this // todo remove?
    }

    override fun onStart() {
        super.onStart()
        mBinding.viewpager.adapter = OnboardingPagerAdapter(
            supportFragmentManager,
            object : OnboardingPagerFragment.ButtonClickListener {
                override fun onButtonClick(buttonType: Int) {
                    when (buttonType) {
                        CLICK_SKIP -> mBinding.viewpager.currentItem = 2
                        CLICK_NEXT -> mBinding.viewpager.currentItem =
                            mBinding.viewpager.currentItem + 1
                        CLICK_START -> finish()
                    }
                }
            })
    }
}