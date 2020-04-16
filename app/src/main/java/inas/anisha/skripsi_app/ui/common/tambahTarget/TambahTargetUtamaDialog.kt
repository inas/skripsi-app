package inas.anisha.skripsi_app.ui.common.tambahTarget

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.databinding.FragmentTambahTargetUtamaBinding
import inas.anisha.skripsi_app.ui.kelolapembelajaran.targetutama.TargetUtamaViewModel
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
        mBinding.buttonAdd.setOnClickListener { addTarget() }
        mBinding.imageviewClose.setOnClickListener { dismiss() }

        mBinding.imageviewRemove.setOnClickListener {
            targetDate = null
            mBinding.edittextDate.setText("")
        }

        mBinding.edittextDate.apply {
            setOnClickListener {
                showDatePicker()
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    showDatePicker()
                    view.clearFocus()
                }
            }
        }

        mBinding.edittextTarget.setText(arguments?.getString("name") ?: "")
        mBinding.edittextNote.setText(arguments?.getString("note") ?: "")
        arguments?.getLong("date")?.takeIf { it != 0L }?.let {
            targetDate = Calendar.getInstance().apply { timeInMillis = it }
            val year = targetDate?.get(Calendar.YEAR)
            val month = targetDate?.get(Calendar.MONTH)
            val day = targetDate?.get(Calendar.DAY_OF_MONTH)
            if (year != null && month != null && day != null) {
                mBinding.edittextDate.setText(day.toString() + "-" + (month + 1) + "-" + year)
            }
        }

        return mBinding.root
    }

    fun addTarget() {
        val targetName = mBinding.edittextTarget.text.toString()
        val targetNote = mBinding.edittextNote.text.toString()
        val targetDate = targetDate

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

    fun showDatePicker() {
//        val builder = MaterialDatePicker.Builder.datePicker()
//        val constraintsBuilder = CalendarConstraints.Builder()
//        constraintsBuilder.setStart(Calendar.getInstance().timeInMillis)
//        builder.setCalendarConstraints(constraintsBuilder.build())
//
//        builder.build().show(childFragmentManager, "")

        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        requireContext().let {
            val datePickerDialog = DatePickerDialog(
                it,
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    targetDate = currentDate.apply { set(year, monthOfYear, dayOfMonth) }
                    mBinding.edittextDate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
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
    }
}