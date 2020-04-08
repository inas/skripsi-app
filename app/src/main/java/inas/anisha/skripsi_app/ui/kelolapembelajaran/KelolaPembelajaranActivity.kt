package inas.anisha.skripsi_app.ui.kelolapembelajaran

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ActivityKelolaPembelajaranBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class KelolaPembelajaranActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityKelolaPembelajaranBinding
    private lateinit var mViewModel: KelolaPembelajaranViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_kelola_pembelajaran)
        mViewModel = ViewModelProviders.of(this).get(KelolaPembelajaranViewModel::class.java)
//        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this // todo remove?
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }
}