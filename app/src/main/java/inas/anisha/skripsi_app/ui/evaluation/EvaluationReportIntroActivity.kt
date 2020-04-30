package inas.anisha.skripsi_app.ui.evaluation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ActivityEvaluationReportIntroBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class EvaluationReportIntroActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityEvaluationReportIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_evaluation_report_intro)
    }

    override fun onStart() {
        super.onStart()
        mBinding.buttonStart.setOnClickListener { goToEvaluationReportActivity() }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private fun goToEvaluationReportActivity() {
        val intent = Intent(this, EvaluationReportActivity::class.java)
        startActivity(intent)
    }
}