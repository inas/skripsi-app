package inas.anisha.srl_app.ui.kelolapembelajaran

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.ActivityKelolaPembelajaranIntroBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class KelolaPembelajaranIntroActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityKelolaPembelajaranIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_kelola_pembelajaran_intro)
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