package inas.anisha.skripsi_app.ui.main.schedule.school

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.data.db.entity.SchoolClassEntity
import inas.anisha.skripsi_app.databinding.ActivitySchoolClassDetailBinding
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class SchoolClassDetailActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySchoolClassDetailBinding
    private lateinit var mViewModel: SchoolClassDetailViewModel
    private lateinit var observable: LiveData<SchoolClassEntity>

    private var schoolClassId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_school_class_detail)
        mViewModel = ViewModelProviders.of(this).get(SchoolClassDetailViewModel::class.java)
        mBinding.lifecycleOwner = this
        schoolClassId = intent.getLongExtra(EXTRA_ID, 0L)
    }

    override fun onStart() {
        super.onStart()
        observable = mViewModel.getSchoolClass(schoolClassId)
        observable.observe(this, Observer {
            it?.let {
                mViewModel.schoolClass = it
                mBinding.viewModel = SchoolClassViewModel()
                    .fromEntity(it)
            }
        })

        mBinding.imageviewBack.setOnClickListener { finish() }
        mBinding.buttonEdit.setOnClickListener {
            openEditSchoolClassDialog()
        }

        mBinding.buttonHapus.setOnClickListener {
            mViewModel.deleteSchoolClass()
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        observable.removeObservers(this)

        mBinding.imageviewBack.setOnClickListener(null)
        mBinding.buttonEdit.setOnClickListener(null)
        mBinding.buttonHapus.setOnClickListener(null)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    fun openEditSchoolClassDialog() {
        val editScheduleDialog = AddSchoolClassDialog()
            .apply {
            arguments = Bundle().apply {
                putParcelable(AddSchoolClassDialog.ARG_CLASS, mViewModel.schoolClass)
            }
        }

        editScheduleDialog.setAddSchoolDialogCallback(object :
            AddSchoolClassDialog.AddSchoolDialogCallback {
            override fun onSubmit(viewModel: SchoolClassViewModel) {
                mViewModel.updateSchoolClass(viewModel)
            }
        })

        editScheduleDialog.show(
            supportFragmentManager,
            AddSchoolClassDialog.TAG
        )
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
}