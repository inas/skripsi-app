package inas.anisha.skripsi_app.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.ActivitySignUpBinding
import inas.anisha.skripsi_app.ui.kelolapembelajaran.KelolaPembelajaranIntroActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class SignUpActivity : AppCompatActivity() {

    lateinit var mBinding: ActivitySignUpBinding
    lateinit var mViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        mViewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)

        val gradeAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.item_dropdown, GRADE_LIST)
        mBinding.dropdownGrade.setAdapter(gradeAdapter)

        val studyAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.item_dropdown, STUDY_LIST)
        mBinding.dropdownStudy.setAdapter(studyAdapter)
    }

    override fun onStart() {
        super.onStart()

        mBinding.dropdownStudy.setOnItemClickListener { parent, view, position, id ->
            mBinding.textlayoutStudyOther.visibility =
                if (position == 2) View.VISIBLE else View.GONE
        }

        mBinding.fabNext.setOnClickListener {
            saveData()
            goToKelolaPembelajaranIntro()
        }
    }

    override fun onStop() {
        super.onStop()
        mBinding.fabNext.setOnClickListener(null)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    private fun saveData() {
        val name = mBinding.edittextName.text.toString()
        val grade = mBinding.dropdownGrade.text.toString()
        var study = mBinding.dropdownStudy.text.toString()
        if (study == STUDY_LIST[2]) study = mBinding.edittextStudyOther.text.toString()

        mViewModel.saveData(name, grade, study)
    }

    private fun goToKelolaPembelajaranIntro() {
        val intent = Intent(this, KelolaPembelajaranIntroActivity::class.java)
        startActivity(intent)
    }

    companion object {
        val GRADE_LIST = mutableListOf("10", "11", "12")
        val STUDY_LIST = mutableListOf("IPA", "IPS", "Lainnya")
    }
}