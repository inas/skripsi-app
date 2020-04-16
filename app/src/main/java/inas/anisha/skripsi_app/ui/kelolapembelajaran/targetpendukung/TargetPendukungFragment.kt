package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentTargetPendukungBinding
import inas.anisha.skripsi_app.databinding.ItemTargetPendukungBinding
import inas.anisha.skripsi_app.ui.kelolapembelajaran.KelolaPembelajaranViewModel

class TargetPendukungFragment : Fragment() {

    private lateinit var mBinding: FragmentTargetPendukungBinding
    private lateinit var mViewModel: KelolaPembelajaranViewModel

    private val recTarget0Vm =
        TargetPendukungViewModel()
            .apply {
                name = "Rutin belajar mandiri"
                note = "Buat sesi khusus untuk belajar mandiri"
                shouldShowSelection = true
            }

    private val recTarget1Vm =
        TargetPendukungViewModel()
            .apply {
                name = "Rutin berlatih"
                note = "Buat sesi khusus untuk berlatih"
                shouldShowSelection = true
            }

    private val recTarget2Vm =
        TargetPendukungViewModel()
            .apply {
                name = "Bentuk keahlian baru"
                note = "Kuasai keahlian baru yang menarik bagimu"
                shouldShowSelection = true
            }

    private var displayedSupportingTargets = mutableListOf<TargetPendukungViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(KelolaPembelajaranViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_target_pendukung, container, false)

        setClickListener()

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    fun initViews() {
        displayedSupportingTargets = mutableListOf(recTarget0Vm, recTarget1Vm, recTarget2Vm)
        displayedSupportingTargets.forEach { vm ->
            val inflater = LayoutInflater.from(context)
            val card: ItemTargetPendukungBinding =
                DataBindingUtil.inflate(inflater, R.layout.item_target_pendukung, null, false)
            card.viewModel = vm
            card.lifecycleOwner = this

            val view = card.root.apply {
                setOnClickListener {
                    vm.isSelected.value = !(vm.isSelected.value ?: false)
                }
            }
            mBinding.layoutRecommended.addView(view)
        }
    }

    fun setClickListener() {
//        mBinding.buttonAddTarget.setOnClickListener { openTambahTargetDialog() }
//        mBinding.layoutTargetRecommendation0.layout.setOnClickListener {
//            selectTarget(
//                false,
//                true,
//                false
//            )
//        }
//        mBinding.layoutTargetRecommendation1.layout.setOnClickListener {
//            selectTarget(
//                false,
//                false,
//                true
//            )
//        }
//
//        mBinding.layoutTargetAdded.imageviewEdit.setOnClickListener { openTambahTargetDialog() }
    }

    fun openTambahTargetDialog() {
//        val tambahTargetDialog = TambahTargetDialog().apply {
//            arguments = Bundle().apply {
//                putString("name", addedTargetVm.name)
//                putString("note", addedTargetVm.note)
//                addedTargetVm.date?.timeInMillis?.let { putLong("date", it) }
//            }
//        }
//        tambahTargetDialog.setOnTargetAddedListener(object :
//            TambahTargetDialog.OnTargetAddedListener {
//            override fun onTargetAdded(target: TargetUtamaViewModel) {
//                addedTargetVm = target
//                mBinding.layoutTargetAdded.viewModel = target
//                mBinding.buttonAddTarget.visibility = View.GONE
//                mBinding.layoutTargetAdded.layout.visibility = View.VISIBLE
//
//                selectTarget(true, false, false)
//            }
//        })
//        tambahTargetDialog.show(childFragmentManager, TambahTargetDialog.TAG)
    }

    fun selectTarget(addedTarget: Boolean, firstRecTarget: Boolean, secondRecTarget: Boolean) {
//        addedTargetVm.isSelected.value = addedTarget
//        recTarget0Vm.isSelected.value = firstRecTarget
//        recTarget1Vm.isSelected.value = secondRecTarget
//
//        when {
//            addedTarget -> mViewModel.mainTarget = addedTargetVm
//            firstRecTarget -> mViewModel.mainTarget = recTarget0Vm
//            secondRecTarget -> mViewModel.mainTarget = recTarget1Vm
//            else -> mViewModel.mainTarget = null
//        }
    }

    companion object {
        const val ADD_TARGET = 1313
        private const val ADDED_TARGET = "ADDED_TARGET"
        private const val REC_TARGET_0 = "REC_TARGET_0"
        private const val REC_TARGET_1 = "REC_TARGET_1"
    }
}