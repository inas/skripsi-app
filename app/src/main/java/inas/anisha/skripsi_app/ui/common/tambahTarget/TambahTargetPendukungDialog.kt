package inas.anisha.skripsi_app.ui.common.tambahTarget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.data.db.entity.TargetPendukungEntity
import inas.anisha.skripsi_app.databinding.FragmentTambahTargetPendukungBinding
import inas.anisha.skripsi_app.ui.common.TextValidator
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetpendukung.TargetPendukungViewModel


class TambahTargetPendukungDialog : DialogFragment() {

    private lateinit var mBinding: FragmentTambahTargetPendukungBinding
    private lateinit var mViewModel: TargetPendukungViewModel
    private var mCallback: OnTargetModifiedListener? = null
    private var mTarget: TargetPendukungEntity = TargetPendukungEntity(0)

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

        mBinding.buttonSave.setOnClickListener { modifyTarget() }
        mBinding.imageviewClose.setOnClickListener { dismiss() }

        initEditText()
        setInitialValue()

        return mBinding.root
    }

    fun initEditText() {
        mBinding.edittextTarget.addTextChangedListener(object :
            TextValidator(mBinding.edittextTarget) {
            override fun validate(textView: TextView, text: String) {
                mBinding.textlayoutTarget.error =
                    if (text.isEmpty()) "Nama target harus diisi" else null
            }
        })
    }

    fun setInitialValue() {
        arguments?.getParcelable<TargetPendukungEntity>(ARG_TARGET)?.let {
            mTarget = it
            mBinding.textviewTitle.text = "Edit Target Pendukung"
            mBinding.edittextTarget.setText(mTarget.name)
            mBinding.edittextNote.setText(mTarget.note)
            mBinding.edittextTime.setText(mTarget.time)
        }
    }

    fun modifyTarget() {
        val targetName = mBinding.edittextTarget.text.toString().trim()
        val targetNote = mBinding.edittextNote.text.toString().trim()
        val targetTime = mBinding.edittextTime.text.toString().trim()

        if (isValid(targetName, targetNote, targetTime)) {
            val target = TargetPendukungViewModel()
                .apply {
                    id = mTarget.id
                    name = targetName
                    note = targetNote
                    time = targetTime
                    isCompleted = mTarget.isCompleted
                }

            mCallback?.let {
                it.onTargetModified(target)
                dismiss()
            }
        }
    }

    private fun isValid(name: String, note: String, time: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            isValid = false
            mBinding.textlayoutTarget.error = "Nama target harus diisi"
        } else {
            mBinding.textlayoutTarget.error = null
        }

        if (name.length > 50 || note.length > 500 || time.length > 50) isValid = false

        return isValid
    }

    fun setOnTargetAddedListener(callback: OnTargetModifiedListener) {
        mCallback = callback
    }

    interface OnTargetModifiedListener {
        fun onTargetModified(target: TargetPendukungViewModel)
    }

    companion object {
        const val TAG = "TAMBAH_TARGET_PENDUKUNG_DIALOG"

        const val ARG_TARGET = "ARG_TARGET"
    }
}