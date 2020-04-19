package inas.anisha.skripsi_app.ui.common.tambahTarget

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentTambahTargetPendukungBinding
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel


class TambahTargetPendukungDialog : DialogFragment() {

    private lateinit var mBinding: FragmentTambahTargetPendukungBinding
    private lateinit var mViewModel: TargetPendukungViewModel
    private var mCallback: OnTargetModifiedListener? = null
    private var targetId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
        mViewModel = ViewModelProviders.of(this).get(TargetPendukungViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tambah_target_pendukung,
                container,
                false
            )
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this

        mBinding.edittextTarget.apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            setRawInputType(InputType.TYPE_CLASS_TEXT)
        }

        mBinding.edittextTime.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setRawInputType(InputType.TYPE_CLASS_TEXT)
        }

        mBinding.buttonSave.setOnClickListener { modifyTarget() }
        mBinding.imageviewClose.setOnClickListener { dismiss() }

        targetId = arguments?.getLong(ARG_ID) ?: 0
        mBinding.edittextTarget.setText(arguments?.getString(ARG_NAME) ?: "")
        mBinding.edittextNote.setText(arguments?.getString(ARG_NOTE) ?: "")
        mBinding.edittextTime.setText(arguments?.getString(ARG_TIME) ?: "")

        return mBinding.root
    }

    fun modifyTarget() {
        val targetName = mBinding.edittextTarget.text.toString()
        val targetNote = mBinding.edittextNote.text.toString()
        val targetTime = mBinding.edittextTime.text.toString()

        val target = TargetPendukungViewModel()
            .apply {
                id = targetId
                name = targetName
                note = targetNote
                time = targetTime
            }

        mCallback?.let {
            it.onTargetModified(target)
            dismiss()
        }

    }

    fun setOnTargetAddedListener(callback: OnTargetModifiedListener) {
        mCallback = callback
    }

    interface OnTargetModifiedListener {
        fun onTargetModified(target: TargetPendukungViewModel)
    }

    companion object {
        const val TAG = "TAMBAH_TARGET_PENDUKUNG_DIALOG"

        const val ARG_ID = "ARG_ID"
        const val ARG_NAME = "ARG_NAME"
        const val ARG_NOTE = "ARG_NOTE"
        const val ARG_TIME = "ARG_TIME"
    }
}