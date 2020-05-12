package inas.anisha.srl_app.ui.onboarding

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.firebase.crashlytics.FirebaseCrashlytics
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.ActivityOnboardingBinding
import inas.anisha.srl_app.ui.signup.SignUpActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper


class OnboardingActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityOnboardingBinding
    private lateinit var mViewModel: OnboardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_onboarding)
        mViewModel = ViewModelProviders.of(this).get(OnboardingViewModel::class.java)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        mBinding.buttonNext.setOnClickListener {
            mBinding.viewpager.setCurrentItem(mBinding.viewpager.currentItem + 1, true)
        }

        mBinding.buttonPrevious.setOnClickListener {
            mBinding.viewpager.setCurrentItem(mBinding.viewpager.currentItem - 1, true)
        }

        mBinding.buttonSkip.setOnClickListener { mBinding.viewpager.setCurrentItem(2, true) }
        mBinding.buttonStart.setOnClickListener {
            val intent = Intent(
                this@OnboardingActivity,
                SignUpActivity::class.java
            )
            startActivity(intent)
        }


        mBinding.viewpager.adapter = OnboardingPagerAdapter(supportFragmentManager)

        mBinding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mViewModel.pageNum.value = position
            }
        })

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {
                        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
                    }
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Izinkan aplikasi mengumpulkan data untuk keperluan penelitian? (Kerahasiaan data dijamin oleh peneliti)")
            .setCancelable(false)
            .setPositiveButton("Izinkan", dialogClickListener)
            .setNegativeButton("Jangan Izinkan", dialogClickListener).show()
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