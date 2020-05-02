package inas.anisha.skripsi_app.ui.common.tambahTarget

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentTambahTargetUtamaBinding
import inas.anisha.skripsi_app.ui.common.TextValidator
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toDateString
import java.util.*


class TambahTargetUtamaDialog : DialogFragment() {

    private lateinit var mBinding: FragmentTambahTargetUtamaBinding
    private var mCallback: OnTargetModifiedListener? = null

    private var targetDate: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tambah_target_utama,
                container,
                false
            )

        initInitialValue()
        initButtonClickListener()
        initEditText()

        return mBinding.root
    }

    private fun initInitialValue() {
        arguments?.getString(ARG_NAME)?.let {
            mBinding.edittextTarget.setText(it)
            mBinding.textviewTitle.text = "Edit Target Utama"
        }

        mBinding.edittextNote.setText(arguments?.getString(ARG_NOTE) ?: "")
        arguments?.getLong(ARG_DATE)?.takeIf { it != 0L }?.let {
            targetDate = Calendar.getInstance().apply { timeInMillis = it }
            targetDate?.let { mBinding.edittextDate.setText(it.toDateString()) }
        }


    }

    private fun initButtonClickListener() {
        mBinding.buttonAdd.setOnClickListener { addTarget() }
        mBinding.imageviewClose.setOnClickListener { dismiss() }

        mBinding.imageviewRemove.setOnClickListener {
            targetDate = null
            mBinding.edittextDate.setText("")
            if (mBinding.edittextDate.hasFocus()) mBinding.edittextDate.clearFocus()
        }
    }

    private fun initEditText() {

        mBinding.edittextDate.apply {
            setOnClickListener { showDatePicker() }
            setOnFocusChangeListener { _, hasFocus -> if (hasFocus) showDatePicker() }
        }

        mBinding.edittextTarget.addTextChangedListener(object :
            TextValidator(mBinding.edittextTarget) {
            override fun validate(textView: TextView, text: String) {
                mBinding.textlayoutTarget.error =
                    if (text.isEmpty()) "Nama target harus diisi" else null
            }
        })
    }

    private fun addTarget() {
        val targetName = mBinding.edittextTarget.text.toString().trim()
        val targetNote = mBinding.edittextNote.text.toString().trim()
        val targetDate = targetDate

        if (isValid(targetName, targetNote)) {
            val target = TargetUtamaViewModel()
                .apply {
                    name = targetName
                    note = targetNote
                    date = targetDate
                }

            mCallback?.let {
                it.onTargetModified(target)
                dismiss()
            }
        }

    }

    private fun isValid(name: String, note: String): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            isValid = false
            mBinding.textlayoutTarget.error = "Nama target harus diisi"
        } else {
            mBinding.textlayoutTarget.error = null
        }

        if (name.length > 50 || note.length > 500) isValid = false

        return isValid
    }

    private fun showDatePicker() {
        val currentDate = targetDate ?: Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        requireContext().let {
            val datePickerDialog = DatePickerDialog(
                it,
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    targetDate = currentDate.apply { set(year, monthOfYear, dayOfMonth) }
                    mBinding.edittextDate.setText(currentDate.toDateString())
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    fun setOnTargetModifiedListener(callback: OnTargetModifiedListener) {
        mCallback = callback
    }

    interface OnTargetModifiedListener {
        fun onTargetModified(target: TargetUtamaViewModel)
    }

    companion object {
        const val TAG = "TAMBAH_TARGET_UTAMA_DIALOG"
        const val ARG_NAME = "ARG_NAME"
        const val ARG_NOTE = "ARG_NOTE"
        const val ARG_DATE = "ARG_DATE"
    }
}