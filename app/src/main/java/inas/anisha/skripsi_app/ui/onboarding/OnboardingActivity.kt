package inas.anisha.skripsi_app.ui.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ActivityOnboardingBinding
import inas.anisha.skripsi_app.ui.kelolapembelajaran.KelolaPembelajaranIntroActivity
import inas.anisha.skripsi_app.ui.onboarding.OnboardingPagerFragment.Companion.CLICK_NEXT
import inas.anisha.skripsi_app.ui.onboarding.OnboardingPagerFragment.Companion.CLICK_SKIP
import inas.anisha.skripsi_app.ui.onboarding.OnboardingPagerFragment.Companion.CLICK_START
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class OnboardingActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
        mBinding.viewpager.adapter = OnboardingPagerAdapter(
            supportFragmentManager,
            object : OnboardingPagerFragment.ButtonClickListener {
                override fun onButtonClick(buttonType: Int) {
                    when (buttonType) {
                        CLICK_SKIP -> mBinding.viewpager.setCurrentItem(2, true)
                        CLICK_NEXT -> mBinding.viewpager.setCurrentItem(
                            mBinding.viewpager.currentItem + 1,
                            true
                        )
                        CLICK_START -> {
                            val intent = Intent(
                                this@OnboardingActivity,
                                KelolaPembelajaranIntroActivity::class.java
                            )
                            startActivity(intent)
                        }
                    }
                }
            })
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    override fun onBackPressed() {
        val currentItem = mBinding.viewpager.currentItem
        if (currentItem > 0) {
            mBinding.viewpager.setCurrentItem(currentItem - 1, true)
        } else {
            finish()
        }
    }
}