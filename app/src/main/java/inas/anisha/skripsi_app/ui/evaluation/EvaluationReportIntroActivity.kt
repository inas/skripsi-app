package inas.anisha.skripsi_app.ui.evaluation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.databinding.ActivityEvaluationReportIntroBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class EvaluationReportIntroActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityEvaluationReportIntroBinding
    private lateinit var mRepository: Repository
    private var observable: LiveData<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_evaluation_report_intro)
        mRepository = Repository.getInstance(application)
    }

    override fun onStart() {
        super.onStart()
        mBinding.buttonStart.setOnClickListener { goToEvaluationReportActivity() }

        observable = mRepository.getCycleCount()
        observable?.observe(this, Observer {
            mBinding.textviewKelolaPembelajaran.text = "Siklus belajar ke-$it sudah berakhir"
            mBinding.textviewDescription.text =
                "Lihat kinerja belajarmu di siklus ke-$it dan tentukan strategi belajar untuk siklus selanjutnya."
        })
    }

    override fun onStop() {
        super.onStop()
        observable?.removeObservers(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private fun goToEvaluationReportActivity() {
        val intent = Intent(this, EvaluationReportActivity::class.java)
        startActivity(intent)
    }
}