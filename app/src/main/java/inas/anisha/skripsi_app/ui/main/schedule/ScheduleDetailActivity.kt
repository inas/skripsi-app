package inas.anisha.skripsi_app.ui.main.schedule

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.databinding.ActivityScheduleDetailBinding
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetPendukungDialog
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class ScheduleDetailActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityScheduleDetailBinding
    private lateinit var mViewModel: ScheduleDetailViewModel
    private lateinit var observable: LiveData<ScheduleEntity>

    private var scheduleId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_schedule_detail)
        mViewModel = ViewModelProviders.of(this).get(ScheduleDetailViewModel::class.java)
        mBinding.lifecycleOwner = this
        scheduleId = intent.getLongExtra(EXTRA_ID, 0L)
    }

    override fun onStart() {
        super.onStart()
        observable = mViewModel.getSchedule(scheduleId)
        observable.observe(this, Observer {
            it?.let {
                mViewModel.schedule = it
                mViewModel.fromEntity(it)
                mBinding.viewModel = mViewModel
            }
        })

        mBinding.imageviewBack.setOnClickListener { finish() }
//        mBinding.buttonMarkAsComplete.setOnClickListener { mViewModel.setTargetAsComplete(true) }
//        mBinding.buttonMarkAsIncomplete.setOnClickListener { mViewModel.setTargetAsComplete(false) }

        mBinding.buttonEdit.setOnClickListener {
//            openModifySupportingTargetDialog(TargetPendukungViewModel().fromEntity(mViewModel.target))
        }

        mBinding.buttonHapus.setOnClickListener {
//            mViewModel.deleteSupportingTarget()
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        observable.removeObservers(this)

        mBinding.imageviewBack.setOnClickListener(null)
//        mBinding.buttonMarkAsComplete.setOnClickListener(null)
//        mBinding.buttonMarkAsIncomplete.setOnClickListener(null)
        mBinding.buttonEdit.setOnClickListener(null)
        mBinding.buttonHapus.setOnClickListener(null)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    fun openModifySupportingTargetDialog(target: TargetPendukungViewModel) {
        val tambahTargetDialog = TambahTargetPendukungDialog().apply {
            arguments = Bundle().apply {
                putParcelable(TambahTargetPendukungDialog.ARG_TARGET, target.toEntity())
            }
        }

        tambahTargetDialog.setOnTargetAddedListener(object :
            TambahTargetPendukungDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetPendukungViewModel) {
//                mViewModel.updateSupportingTarget(target)
            }
        })

        tambahTargetDialog.show(supportFragmentManager, TambahTargetPendukungDialog.TAG)
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
}