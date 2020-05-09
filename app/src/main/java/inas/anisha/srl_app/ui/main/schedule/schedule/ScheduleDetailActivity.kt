package inas.anisha.srl_app.ui.main.schedule.schedule

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.srl_app.R
import inas.anisha.srl_app.data.db.entity.ScheduleEntity
import inas.anisha.srl_app.databinding.ActivityScheduleDetailBinding
import inas.anisha.srl_app.utils.ViewUtil.Companion.strikeThrough
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class ScheduleDetailActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityScheduleDetailBinding
    private lateinit var mViewModel: ScheduleDetailViewModel

    private var observable: LiveData<ScheduleEntity>? = null
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
        observable?.observe(this, Observer {
            it?.let {
                mViewModel.schedule = it
                mBinding.viewModel = ScheduleViewModel().fromEntity(it)
                mBinding.textviewTitle.strikeThrough(it.isCompleted)
            }
        })

        mBinding.imageviewBack.setOnClickListener { finish() }
        mBinding.buttonMarkAsComplete.setOnClickListener { mViewModel.updateScheduleAsComplete(true) }
        mBinding.buttonMarkAsIncomplete.setOnClickListener {
            mViewModel.updateScheduleAsComplete(
                false
            )
        }

        mBinding.buttonEdit.setOnClickListener {
            openEditScheduleDialog()
        }

        mBinding.buttonHapus.setOnClickListener {
            mViewModel.deleteTask()
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        observable?.removeObservers(this)

        mBinding.imageviewBack.setOnClickListener(null)
        mBinding.buttonMarkAsComplete.setOnClickListener(null)
        mBinding.buttonMarkAsIncomplete.setOnClickListener(null)
        mBinding.buttonEdit.setOnClickListener(null)
        mBinding.buttonHapus.setOnClickListener(null)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    fun openEditScheduleDialog() {
        val editScheduleDialog = AddScheduleDialog()
            .apply {
            arguments = Bundle().apply {
                putParcelable(AddScheduleDialog.ARG_SCHEDULE, mViewModel.schedule)
            }
        }

        editScheduleDialog.setAddScheduleDialogListener(object :
            AddScheduleDialog.AddScheduleDialogListener {
            override fun onScheduleModified(schedule: ScheduleViewModel) {
                mViewModel.updateSchedule(schedule)
            }
        })

        editScheduleDialog.show(supportFragmentManager, AddScheduleDialog.TAG)
    }

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }
}