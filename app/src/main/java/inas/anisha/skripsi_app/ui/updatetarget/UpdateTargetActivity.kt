package inas.anisha.skripsi_app.ui.updatetarget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity
import inas.anisha.skripsi_app.databinding.ActivityUpdateTargetBinding
import inas.anisha.skripsi_app.ui.common.atursiklus.AturSiklusDialogFragment
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetPendukungDialog
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetUtamaDialog
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel
import inas.anisha.skripsi_app.ui.main.target.TargetPendukungAdapter
import inas.anisha.skripsi_app.ui.main.target.TargetPendukungDetailActivity
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class UpdateTargetActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityUpdateTargetBinding
    private lateinit var mViewModel: UpdateTargetViewModel
    private lateinit var supportingTargetsAdapter: TargetPendukungAdapter

    private var mainTargetObservable: LiveData<TargetUtamaEntity>? = null
    private var supportingTargetsObservable: LiveData<List<TargetPendukungViewModel>>? = null
    private var cycleCountObservable: LiveData<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(UpdateTargetViewModel::class.java)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_target)
    }

    override fun onStart() {
        super.onStart()

        mBinding.buttonEditMainTarget.setOnClickListener { openModifyMainTargetDialog() }
        mBinding.buttonEditCycle.setOnClickListener { openSetCycleDialog() }
        mBinding.buttonAddSupportingTarget.setOnClickListener { openAddTargetPendukungDialog() }

        initMainTarget()
        initCycleTime()
        initSupportingTarget()
    }

    override fun onStop() {
        super.onStop()
        mainTargetObservable?.removeObservers(this)
        supportingTargetsObservable?.removeObservers(this)
        cycleCountObservable?.removeObservers(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    fun initMainTarget() {
        mainTargetObservable = mViewModel.getMainTarget()
        mainTargetObservable?.observe(this, Observer {
            mViewModel.mainTarget = TargetUtamaViewModel().fromEntity(it)
            mBinding.layoutMainTarget.viewModel = mViewModel.mainTarget
        })
    }

    fun initCycleTime() {
        val cycleTime = mViewModel.getCycleTime()
        mViewModel.setCycleTime(cycleTime)

        cycleCountObservable = mViewModel.getCycleCount()
        cycleCountObservable?.observe(this, Observer {
            mViewModel.cycleNumber = it
        })
    }

    fun initSupportingTarget() {
        supportingTargetsAdapter = TargetPendukungAdapter().apply {
            setItemListener(object : TargetPendukungAdapter.ItemListener {
                override fun onItemClick(viewModel: TargetPendukungViewModel) {
                    openTargetPendukungDetail(viewModel.id)
                }
            })
            setHasStableIds(false)
        }
        mBinding.recyclerViewSupportingTarget.adapter = supportingTargetsAdapter

        supportingTargetsObservable = mViewModel.getSupportingTargets()
        supportingTargetsObservable?.observe(this, Observer { targets ->
            mViewModel.supportingTargets = targets as MutableList<TargetPendukungViewModel>
            supportingTargetsAdapter.setTargets(targets)
        })

    }

    fun openModifyMainTargetDialog() {
        val tambahTargetDialog = TambahTargetUtamaDialog().apply {
            arguments = Bundle().apply {
                putString("name", mViewModel.mainTarget.name)
                putString("note", mViewModel.mainTarget.note)
                mViewModel.mainTarget.date?.timeInMillis?.let { putLong("date", it) }
            }
        }

        tambahTargetDialog.setOnTargetModifiedListener(object :
            TambahTargetUtamaDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetUtamaViewModel) {
                mViewModel.saveMainTarget(target)
            }
        })

        tambahTargetDialog.show(supportFragmentManager, TambahTargetUtamaDialog.TAG)
    }

    fun openSetCycleDialog() {
        val aturSiklusDialog = AturSiklusDialogFragment().apply {
            arguments = Bundle().apply {
                putString(
                    AturSiklusDialogFragment.ARG_FREQUENCY,
                    SkripsiConstant.getCycleFrequencyString(mViewModel.frequency)
                )
                putString(AturSiklusDialogFragment.ARG_DURATION, mViewModel.duration.toString())
            }

            setOnSiklusChosenListener(object : AturSiklusDialogFragment.OnSiklusChosenListener {
                override fun onTargetAdded(cycleTime: Pair<Int, Int>) {
                    mViewModel.setCycleTime(cycleTime)
                }
            })
        }
        aturSiklusDialog.show(supportFragmentManager, AturSiklusDialogFragment.TAG)
    }

    fun openAddTargetPendukungDialog() {
        val tambahTargetDialog = TambahTargetPendukungDialog()
        tambahTargetDialog.setOnTargetAddedListener(object :
            TambahTargetPendukungDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetPendukungViewModel) {
                mViewModel.addOrUpdateSupportingTarget(target)
            }
        })

        tambahTargetDialog.show(supportFragmentManager, TambahTargetPendukungDialog.TAG)
    }

    fun openTargetPendukungDetail(targetId: Long) {
        val intent = Intent(this, TargetPendukungDetailActivity::class.java).apply {
            putExtra(TargetPendukungDetailActivity.EXTRA_ID, targetId)
        }
        startActivity(intent)
    }

    companion object {
        const val TAG = "TARGET_FRAGMENT"
    }
}