package inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentTargetUtamaBinding
import inas.anisha.skripsi_app.databinding.ItemCardBigBinding
import inas.anisha.skripsi_app.ui.common.tambahTarget.TambahTargetUtamaDialog
import inas.anisha.skripsi_app.ui.kelolapembelajaran.KelolaPembelajaranViewModel

class TargetUtamaFragment : Fragment() {

    private lateinit var mBinding: FragmentTargetUtamaBinding
    private lateinit var mViewModel: KelolaPembelajaranViewModel

    private val recTarget0Vm =
        TargetUtamaViewModel()
            .apply {
            name = "Melanjutkan pendidikan"
            note = "Setelah lulus saya ingin melanjutkan sekolah ke universitas impian"
                shouldShowSelection = true
        }

    private val recTarget1Vm =
        TargetUtamaViewModel()
            .apply {
            name = "Mewujudkan cita-cita saya"
            note = "Saya ingin memiliki pekerjaan yang saya impikan"
                shouldShowSelection = true
        }

    private var addedTargetVm: TargetUtamaViewModel =
        TargetUtamaViewModel()

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
            DataBindingUtil.inflate(inflater, R.layout.fragment_target_utama, container, false)
        mBinding.viewModel = mViewModel

        initViews()
        setClickListener()

        return mBinding.root
    }

    fun initViews() {
        mBinding.layoutTargetAdded.viewModel = addedTargetVm
        mBinding.layoutTargetAdded.lifecycleOwner = this
        mBinding.layoutTargetRecommendation0.viewModel = recTarget0Vm
        mBinding.layoutTargetRecommendation0.lifecycleOwner = this
        mBinding.layoutTargetRecommendation1.viewModel = recTarget1Vm
        mBinding.layoutTargetRecommendation1.lifecycleOwner = this
    }

    fun setClickListener() {
        mBinding.buttonAddTarget.setOnClickListener { openTambahTargetDialog() }

        mBinding.layoutTargetAdded.layoutCard.setOnClickListener {
            selectTarget(true, false, false)
            modifyTargetDialog(addedTargetVm, mBinding.layoutTargetAdded)
        }

        mBinding.layoutTargetRecommendation0.layoutCard.setOnClickListener {
            selectTarget(false, true, false)
            modifyTargetDialog(recTarget0Vm, mBinding.layoutTargetRecommendation0)
        }

        mBinding.layoutTargetRecommendation1.layoutCard.setOnClickListener {
            selectTarget(false, false, true)
            modifyTargetDialog(recTarget1Vm, mBinding.layoutTargetRecommendation1)
        }

        mBinding.layoutTargetAdded.layout.setOnClickListener { selectTarget(true, false, false) }
        mBinding.layoutTargetRecommendation0.layout.setOnClickListener {
            selectTarget(
                false,
                true,
                false
            )
        }
        mBinding.layoutTargetRecommendation1.layout.setOnClickListener {
            selectTarget(
                false,
                false,
                true
            )
        }

    }

    fun openTambahTargetDialog() {
        val tambahTargetDialog = TambahTargetUtamaDialog()
        tambahTargetDialog.setOnTargetAddedListener(object :
            TambahTargetUtamaDialog.OnTargetAddedListener {
            override fun onTargetAdded(target: TargetUtamaViewModel) {
                addedTargetVm = target
                mBinding.layoutTargetAdded.viewModel = target
                mBinding.buttonAddTarget.visibility = View.GONE
                mBinding.layoutTargetAdded.layout.visibility = View.VISIBLE

                selectTarget(true, false, false)
            }
        })
        tambahTargetDialog.show(childFragmentManager, TambahTargetUtamaDialog.TAG)
    }


    fun modifyTargetDialog(targetVm: TargetUtamaViewModel, clickedBinding: ItemCardBigBinding) {
        val tambahTargetDialog = TambahTargetUtamaDialog().apply {
            arguments = Bundle().apply {
                putString("name", targetVm.name)
                putString("note", targetVm.note)
                targetVm.date?.timeInMillis?.let { putLong("date", it) }
            }
        }

        tambahTargetDialog.setOnTargetAddedListener(object :
            TambahTargetUtamaDialog.OnTargetAddedListener {
            override fun onTargetAdded(target: TargetUtamaViewModel) {
                targetVm.replaceValues(target)
                clickedBinding.viewModel = targetVm
                mViewModel.mainTarget = target
            }
        })

        tambahTargetDialog.show(childFragmentManager, TambahTargetUtamaDialog.TAG)
    }

    fun selectTarget(addedTarget: Boolean, firstRecTarget: Boolean, secondRecTarget: Boolean) {
        addedTargetVm.isSelected.value = addedTarget
        recTarget0Vm.isSelected.value = firstRecTarget
        recTarget1Vm.isSelected.value = secondRecTarget
    }

    companion object {
        const val ADD_TARGET = 1313
        private const val ADDED_TARGET = "ADDED_TARGET"
        private const val REC_TARGET_0 = "REC_TARGET_0"
        private const val REC_TARGET_1 = "REC_TARGET_1"
    }
}