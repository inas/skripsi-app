package inas.anisha.skripsi_app.ui.updatetarget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.data.db.entity.TargetUtamaEntity
import inas.anisha.skripsi_app.databinding.ActivityUpdateTargetBinding
import inas.anisha.skripsi_app.ui.common.atursiklus.AturSiklusDialogFragment
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetPendukungDialog
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetUtamaDialog
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel
import inas.anisha.skripsi_app.ui.main.target.TargetPendukungAdapter
import inas.anisha.skripsi_app.ui.main.target.TargetPendukungDetailActivity
import inas.anisha.skripsi_app.ui.main.target.TargetPendukungDetailActivity.Companion.EXTRA_ENTITY
import inas.anisha.skripsi_app.ui.main.target.TargetPendukungDetailActivity.Companion.RESULT_DELETED
import inas.anisha.skripsi_app.ui.main.target.TargetPendukungDetailActivity.Companion.RESULT_UPDATED
import io.github.inflationx.viewpump.ViewPumpContextWrapper

class UpdateTargetActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityUpdateTargetBinding
    private lateinit var mViewModel: UpdateTargetViewModel
    private lateinit var supportingTargetsAdapter: TargetPendukungAdapter

    private var mainTargetObservable: LiveData<TargetUtamaEntity>? = null
    private var cycleCountObservable: LiveData<Int>? = null

    private var openedTarget: TargetPendukungViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(UpdateTargetViewModel::class.java)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_target)
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        mBinding.buttonEditMainTarget.setOnClickListener { openModifyMainTargetDialog() }
        mBinding.buttonEditCycle.setOnClickListener { openSetCycleDialog() }
        mBinding.buttonAddSupportingTarget.setOnClickListener { openAddTargetPendukungDialog() }

        initMainTarget()
        initCycleTime()
        initSupportingTarget()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainTargetObservable?.removeObservers(this)
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
                    openTargetPendukungDetail(viewModel)
                }
            })
            setHasStableIds(false)
        }
        mBinding.recyclerViewSupportingTarget.adapter = supportingTargetsAdapter

        mViewModel.getSupportingTargets().observeOnce(this, { targets ->
            mViewModel.supportingTargets = targets as MutableList<TargetPendukungViewModel>
            mViewModel.shouldShowSupportingTargets.value = targets.isNotEmpty()
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
                mViewModel.mainTarget = target
                mBinding.layoutMainTarget.viewModel = target
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
                mViewModel.supportingTargets.add(target)
                mViewModel.shouldShowSupportingTargets.value =
                    mViewModel.supportingTargets.isNotEmpty()
            }
        })

        tambahTargetDialog.show(supportFragmentManager, TambahTargetPendukungDialog.TAG)
    }

    fun openTargetPendukungDetail(target: TargetPendukungViewModel) {
        openedTarget = target
        val intent = Intent(this, TargetPendukungDetailActivity::class.java).apply {
            putExtra(EXTRA_ENTITY, target.toEntity())
        }
        startActivityForResult(intent, TARGET_DETAIL_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TARGET_DETAIL_REQUEST) {
            if (resultCode == RESULT_DELETED) {
                mViewModel.supportingTargets.remove(openedTarget)
            } else if (resultCode == RESULT_UPDATED) {
                data?.getParcelableExtra<TargetPendukungEntity>(EXTRA_ENTITY)
                    ?.let { openedTarget?.fromEntity(it) }
            }
            supportingTargetsAdapter.setTargets(mViewModel.supportingTargets)
            openedTarget = null
        }
    }

    fun <T> LiveData<T>.observeOnce(
        owner: LifecycleOwner,
        reactToChange: (T) -> Unit
    ): Observer<T> {
        val wrappedObserver = object : Observer<T> {
            override fun onChanged(data: T) {
                reactToChange(data)
                removeObserver(this)
            }
        }

        observe(owner, wrappedObserver)
        return wrappedObserver
    }

    companion object {
        const val TAG = "TARGET_FRAGMENT"
        const val TARGET_DETAIL_REQUEST = 27
    }
}