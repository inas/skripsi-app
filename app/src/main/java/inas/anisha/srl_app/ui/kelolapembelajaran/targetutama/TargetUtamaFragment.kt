package inas.anisha.srl_app.ui.kelolapembelajaran.targetutama

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.FragmentTargetUtamaBinding
import inas.anisha.srl_app.databinding.ItemCardBigBinding
import inas.anisha.srl_app.ui.common.tambahTarget.TambahTargetUtamaDialog
import inas.anisha.srl_app.ui.kelolapembelajaran.KelolaPembelajaranViewModel

class TargetUtamaFragment : Fragment() {

    private lateinit var mBinding: FragmentTargetUtamaBinding
    private lateinit var mViewModel: KelolaPembelajaranViewModel

    private val recTarget0Vm =
        TargetUtamaViewModel()
            .apply {
                name = "Melanjutkan pendidikan"
                note = "Setelah lulus saya ingin melanjutkan sekolah ke universitas impian"
                shouldShowSelection = true
                isSelected.value = true
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_target_utama, container, false)
        mBinding.viewModel = mViewModel
        mViewModel.mainTarget = recTarget0Vm

        initViews()
        setClickListener()

        return mBinding.root
    }

    fun initViews() {
        mBinding.layoutTargetAdded.viewModel = addedTargetVm
        mBinding.layoutTargetAdded.lifecycleOwner = this

        mBinding.layoutTargetRecommendation0.viewModel = recTarget0Vm
        mBinding.layoutTargetRecommendation0.lifecycleOwner = this
        mBinding.layoutTargetRecommendation0.imageviewTarget.setBackgroundResource(R.drawable.bg_rounded_blue)
        mBinding.layoutTargetRecommendation0.imageviewIllustration.setBackgroundResource(R.drawable.bg_main_target_education)
        mBinding.layoutTargetRecommendation0.imageviewIllustration.visibility = View.VISIBLE

        mBinding.layoutTargetRecommendation1.viewModel = recTarget1Vm
        mBinding.layoutTargetRecommendation1.lifecycleOwner = this
        mBinding.layoutTargetRecommendation1.imageviewTarget.setBackgroundResource(R.drawable.bg_rounded_yellow)
        mBinding.layoutTargetRecommendation1.imageviewIllustration.setBackgroundResource(R.drawable.bg_main_target_wish)
        mBinding.layoutTargetRecommendation1.imageviewIllustration.visibility = View.VISIBLE
    }

    fun setClickListener() {
        mBinding.buttonAddTarget.setOnClickListener { openTambahTargetDialog() }

        mBinding.layoutTargetAdded.layoutCard.setOnClickListener {
            selectTarget(true, false, false)
            openModifyTargetDialog(addedTargetVm, mBinding.layoutTargetAdded)
        }

        mBinding.layoutTargetRecommendation0.layoutCard.setOnClickListener {
            selectTarget(false, true, false)
            openModifyTargetDialog(recTarget0Vm, mBinding.layoutTargetRecommendation0)
        }

        mBinding.layoutTargetRecommendation1.layoutCard.setOnClickListener {
            selectTarget(false, false, true)
            openModifyTargetDialog(recTarget1Vm, mBinding.layoutTargetRecommendation1)
        }

        mBinding.layoutTargetAdded.layout.setOnClickListener {
            mViewModel.mainTarget = addedTargetVm
            selectTarget(true, false, false)
        }

        mBinding.layoutTargetRecommendation0.layout.setOnClickListener {
            mViewModel.mainTarget = recTarget0Vm
            selectTarget(false, true, false)
        }
        mBinding.layoutTargetRecommendation1.layout.setOnClickListener {
            mViewModel.mainTarget = recTarget1Vm
            selectTarget(false, false, true)
        }

    }

    fun openTambahTargetDialog() {
        val tambahTargetDialog = TambahTargetUtamaDialog()
        tambahTargetDialog.setOnTargetModifiedListener(object :
            TambahTargetUtamaDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetUtamaViewModel) {
                addedTargetVm = target.apply { shouldShowSelection = true }
                mBinding.layoutTargetAdded.viewModel = target
                mBinding.buttonAddTarget.visibility = View.GONE
                mBinding.layoutTargetAdded.layout.visibility = View.VISIBLE

                mViewModel.mainTarget = addedTargetVm
                selectTarget(true, false, false)
            }
        })
        tambahTargetDialog.show(childFragmentManager, TambahTargetUtamaDialog.TAG)
    }


    fun openModifyTargetDialog(targetVm: TargetUtamaViewModel, clickedBinding: ItemCardBigBinding) {
        val tambahTargetDialog = TambahTargetUtamaDialog().apply {
            arguments = Bundle().apply {
                putString(TambahTargetUtamaDialog.ARG_NAME, targetVm.name)
                putString(TambahTargetUtamaDialog.ARG_NOTE, targetVm.note)
                targetVm.date?.timeInMillis?.let { putLong(TambahTargetUtamaDialog.ARG_DATE, it) }
            }
        }

        tambahTargetDialog.setOnTargetModifiedListener(object :
            TambahTargetUtamaDialog.OnTargetModifiedListener {
            override fun onTargetModified(target: TargetUtamaViewModel) {
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

}