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
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetPendukungDialog
import inas.anisha.skripsi_app.ui.kelolapembelajaran.KelolaPembelajaranViewModel

class TargetPendukungFragment : Fragment() {

    private lateinit var mBinding: FragmentTargetPendukungBinding
    private lateinit var mViewModel: KelolaPembelajaranViewModel

    private val recTarget0Vm =
        TargetPendukungViewModel()
            .apply {
                name = "Rutin belajar mandiri"
                note = "Buat sesi khusus untuk belajar mandiri"
            }

    private val recTarget1Vm =
        TargetPendukungViewModel()
            .apply {
                name = "Rutin berlatih"
                note = "Buat sesi khusus untuk berlatih"
            }

    private val recTarget2Vm =
        TargetPendukungViewModel()
            .apply {
                name = "Bentuk keahlian baru"
                note = "Kuasai keahlian baru yang menarik bagimu"
            }

    private var displayedSupportingTargets: MutableMap<String, TargetPendukungViewModel> =
        mutableMapOf()
    private var idCounter = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().let {
            mViewModel = ViewModelProviders.of(it).get(KelolaPembelajaranViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_target_pendukung, container, false)

        initViews()

        return mBinding.root
    }

    fun initViews() {
        mBinding.buttonAddTarget.setOnClickListener { openAddTargetDialog() }

        displayedSupportingTargets = mutableMapOf(
            Pair("recommended0", recTarget0Vm),
            Pair("recommended1", recTarget1Vm),
            Pair("recommended2", recTarget2Vm)
        )

        displayedSupportingTargets.values.forEachIndexed { index, vm ->
            val inflater = LayoutInflater.from(context)
            val card: ItemTargetPendukungBinding =
                DataBindingUtil.inflate(inflater, R.layout.item_target_pendukung, null, false)
            card.viewModel = vm
            card.lifecycleOwner = this
            card.layoutCard.setOnClickListener {
                vm.isSelected.value = true
                openModifyTargetDialog(vm, card)
            }

            val view = card.layoutContainer.apply {
                setOnClickListener {
                    vm.isSelected.value = !(vm.isSelected.value ?: false)
                }
            }
            mBinding.layoutRecommended.addView(view)
        }
    }

    fun openAddTargetDialog() {
        val tambahTargetDialog = TambahTargetPendukungDialog()
        tambahTargetDialog.setOnTargetAddedListener(object :
            TambahTargetPendukungDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetPendukungViewModel) {
                target.isSelected.value = true

                val inflater = LayoutInflater.from(context)
                val card: ItemTargetPendukungBinding =
                    DataBindingUtil.inflate(inflater, R.layout.item_target_pendukung, null, false)
                card.viewModel = target
                card.lifecycleOwner = this@TargetPendukungFragment

                val view = card.layoutContainer.apply {
                    setOnClickListener {
                        target.isSelected.value = !(target.isSelected.value ?: false)
                    }

                    tag = "added" + idCounter++
                }

                card.layoutCard.setOnClickListener {
                    target.isSelected.value = true
                    openModifyTargetDialog(target, card)
                }

                card.imageviewDelete.setOnClickListener {
                    mBinding.layoutAdded.removeView(view)
                    displayedSupportingTargets.remove(view.tag)
                }

                displayedSupportingTargets[view.tag.toString()] = target
                mBinding.layoutAdded.addView(view, 0)
            }
        })

        tambahTargetDialog.show(childFragmentManager, TambahTargetPendukungDialog.TAG)
    }

    fun openModifyTargetDialog(
        targetVm: TargetPendukungViewModel,
        clickedBinding: ItemTargetPendukungBinding
    ) {
        val tambahTargetDialog = TambahTargetPendukungDialog().apply {
            arguments = Bundle().apply {
                putString(TambahTargetPendukungDialog.ARG_NAME, targetVm.name)
                putString(TambahTargetPendukungDialog.ARG_NOTE, targetVm.note)
                putString(TambahTargetPendukungDialog.ARG_TIME, targetVm.time)
            }
        }

        tambahTargetDialog.setOnTargetAddedListener(object :
            TambahTargetPendukungDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetPendukungViewModel) {
                targetVm.replaceValues(target)
                targetVm.isSelected.value = true
                clickedBinding.viewModel = targetVm
            }
        })

        tambahTargetDialog.show(childFragmentManager, TambahTargetPendukungDialog.TAG)
    }

    fun getSelectedTargets() {
        mViewModel.supportingTargets =
            displayedSupportingTargets.values.filter { it.isSelected.value ?: false }
                .toMutableList()
    }

}