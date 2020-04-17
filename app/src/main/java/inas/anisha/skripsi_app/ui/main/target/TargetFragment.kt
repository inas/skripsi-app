package inas.anisha.skripsi_app.ui.main.target

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.databinding.FragmentPageTargetBinding
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetPendukungDialog
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetUtamaDialog
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel

class TargetFragment : Fragment() {

    private lateinit var mBinding: FragmentPageTargetBinding
    private lateinit var mViewModel: TargetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(TargetViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_target, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.buttonAdd.setOnClickListener { openAddTargetPendukungDialog() }
        mBinding.layoutTargetUtama.layout.setOnClickListener { openModifyMainTargetDialog() }

        mBinding.textviewSupportingTarget.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                requireContext().let {
                    mBinding.scrollview.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val rvHeight = mBinding.scrollview.height - mBinding.recyclerviewTarget.top
                    mBinding.recyclerviewTarget.minimumHeight = rvHeight
                }
            }
        })


        initMainTarget()
        initCycleTime()
        initSupportingTarget()
    }

    fun initMainTarget() {
        mViewModel.getMainTarget().observe(this, Observer {
            mBinding.layoutTargetUtama.viewModel = mViewModel.getMainTargetViewModel(it)
        })
    }

    fun initCycleTime() {
        val cycleTime = mViewModel.getCycleTime()

        mBinding.textviewCycleDescription.text = "Evaluasi berikutnya: " + "TODO tanggal berapa"
        mBinding.textviewCycle.text = "" + cycleTime.second + " " +
                when (cycleTime.first) {
                    SkripsiConstant.CYCLE_FREQUENCY_DAILY -> "Harian"
                    SkripsiConstant.CYCLE_FREQUENCY_WEEKLY -> "Mingguan"
                    else -> "Bulanan"
                }
    }

    fun initSupportingTarget() {
        val adapter = TargetPendukungRecyclerViewAdapter().apply {
            setItemListener(object : TargetPendukungRecyclerViewAdapter.ItemListener {
                override fun onItemClick(target: TargetPendukungViewModel) {
                    openModifySupportingTargetDialog(target)
                }

                override fun onItemDeleted(target: TargetPendukungViewModel) {
                    deleteTarget(target)
                }
            })
            setHasStableIds(true)
        }
        mBinding.recyclerviewTarget.adapter = adapter

        mViewModel.getSupportingTargets().observe(this, Observer { targets ->
            mBinding.textviewSupportingTarget.visibility =
                if (targets.isNotEmpty()) View.VISIBLE else View.GONE
            adapter.setTargets(targets.reversed())
        })
    }

    fun openAddTargetPendukungDialog() {
        val tambahTargetDialog = TambahTargetPendukungDialog()
        tambahTargetDialog.setOnTargetAddedListener(object :
            TambahTargetPendukungDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetPendukungViewModel) {
                target.shouldShowSelection
                mViewModel.addOrUpdateSupportingTarget(target)
            }
        })

        tambahTargetDialog.show(childFragmentManager, TambahTargetPendukungDialog.TAG)
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
                mViewModel.updateMainTarget(target)
            }
        })

        tambahTargetDialog.show(childFragmentManager, TambahTargetUtamaDialog.TAG)
    }

    fun openModifySupportingTargetDialog(target: TargetPendukungViewModel) {
        val tambahTargetDialog = TambahTargetPendukungDialog().apply {
            arguments = Bundle().apply {
                putLong(TambahTargetPendukungDialog.ARG_ID, target.id)
                putString(TambahTargetPendukungDialog.ARG_NAME, target.name)
                putString(TambahTargetPendukungDialog.ARG_NOTE, target.note)
                putString(TambahTargetPendukungDialog.ARG_TIME, target.time)
            }
        }

        tambahTargetDialog.setOnTargetAddedListener(object :
            TambahTargetPendukungDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetPendukungViewModel) {
                mViewModel.addOrUpdateSupportingTarget(target)
            }
        })

        tambahTargetDialog.show(childFragmentManager, TambahTargetPendukungDialog.TAG)
    }

    fun deleteTarget(target: TargetPendukungViewModel) {
        mViewModel.deleteSupportingTarget(target.id)
    }
}