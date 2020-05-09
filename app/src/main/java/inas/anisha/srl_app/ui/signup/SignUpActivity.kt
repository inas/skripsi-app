package inas.anisha.srl_app.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.ActivitySignUpBinding
import inas.anisha.srl_app.ui.common.TextValidator
import inas.anisha.srl_app.ui.kelolapembelajaran.KelolaPembelajaranIntroActivity
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

        mBinding.dropdownStudy.setOnItemClickListener { _, _, position, _ ->
            mBinding.textlayoutStudyOther.visibility =
                if (position == 2) View.VISIBLE else View.GONE
        }

        mBinding.buttonAdd.setOnClickListener {
            val name = mBinding.edittextName.text.toString().trim()
            val grade = mBinding.dropdownGrade.text.toString()
            var study = mBinding.dropdownStudy.text.toString()
            if (study == STUDY_LIST[2]) study = mBinding.edittextStudyOther.text.toString().trim()

            if (isValid(name, study)) {
                mViewModel.saveData(name, grade, study)
                goToKelolaPembelajaranIntro()
            }
        }

        mBinding.edittextName.addTextChangedListener(object : TextValidator(mBinding.edittextName) {
            override fun validate(textView: TextView, text: String) {
                mBinding.textlayoutName.error = if (text.isEmpty()) "Nama harus diisi" else null
            }
        })

        mBinding.edittextName.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
        }

        mBinding.edittextStudyOther.addTextChangedListener(object :
            TextValidator(mBinding.edittextStudyOther) {
            override fun validate(textView: TextView, text: String) {
                mBinding.textlayoutStudyOther.error =
                    if (text.isEmpty()) "Jurusan harus diisi" else null
            }
        })
    }

    override fun onStop() {
        super.onStop()
        mBinding.buttonAdd.setOnClickListener(null)
        mBinding.dropdownStudy.onItemClickListener = null
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    fun isValid(name: String, study: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            isValid = false
            mBinding.textlayoutName.error = "Nama harus diisi"
        } else {
            mBinding.textlayoutName.error = null
        }

        if (study.isEmpty()) {
            isValid = false
            mBinding.textlayoutStudyOther.error = "Jurusan harus diisi"
        } else {
            mBinding.textlayoutStudyOther.error = null
        }

        return isValid
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