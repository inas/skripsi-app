package inas.anisha.srl_app.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.ActivitySplashScreenBinding
import inas.anisha.srl_app.ui.main.MainActivity
import inas.anisha.srl_app.ui.onboarding.OnboardingActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivitySplashScreenBinding
    private lateinit var mViewModel: SplashScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        mViewModel = ViewModelProviders.of(this).get(SplashScreenViewModel::class.java)

        if (mViewModel.shouldShowKelolaPembelajaran()) {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}