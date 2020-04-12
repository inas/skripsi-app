package inas.anisha.skripsi_app.ui.common.addTarget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentTargetUtamaBinding

class TambahTargetFragment : DialogFragment() {

    private lateinit var mBinding: FragmentTargetUtamaBinding
//    private lateinit var mViewModel: KelolaPembelajaranViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tambah_target, container, false)
//        mBinding.viewModel = mViewModel

//        initViews()
//        setClickListener()

        return mBinding.root
    }
}