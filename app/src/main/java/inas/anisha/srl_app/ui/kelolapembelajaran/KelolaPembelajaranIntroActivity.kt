package inas.anisha.srl_app.ui.kelolapembelajaran

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.analytics.FirebaseAnalytics
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.ActivityKelolaPembelajaranIntroBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class KelolaPembelajaranIntroActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityKelolaPembelajaranIntroBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_kelola_pembelajaran_intro)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, null)
    }

    override fun onStart() {
        super.onStart()
        mBinding.buttonStart.setOnClickListener { goToKelolaPembelajaran() }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private fun goToKelolaPembelajaran() {
        val intent = Intent(this, KelolaPembelajaranActivity::class.java)
        startActivity(intent)
    }
}