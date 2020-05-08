package inas.anisha.skripsi_app.ui.main.schedule.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.constant.SkripsiConstant.Companion.SCHEDULE_REMINDER_UNIT_DAYS
import inas.anisha.skripsi_app.constant.SkripsiConstant.Companion.SCHEDULE_REMINDER_UNIT_HOURS
import inas.anisha.skripsi_app.constant.SkripsiConstant.Companion.SCHEDULE_REMINDER_UNIT_MINUTES
import inas.anisha.skripsi_app.data.db.entity.ReminderEntity
import inas.anisha.skripsi_app.databinding.FragmentAddScheduleReminderBinding

class AddScheduleReminderDialog : DialogFragment() {

    private lateinit var mBinding: FragmentAddScheduleReminderBinding

    private var mCallback: AddScheduleReminderDialogListener? = null
    private var unitString: List<String> = mutableListOf()
    private var mReminder: ReminderEntity? = null
    private var mScheduleId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_schedule_reminder,
                container,
                false
            )

        arguments?.getParcelable<ReminderEntity>(ARG_REMINDER)?.let {
            mReminder = it
            mBinding.edittextAmount.setText("${it.amount}")
            mBinding.switchPopup.isChecked = it.isPopup

            mBinding.dropdownUnit.setText(
                SkripsiConstant.getScheduleReminderUnitString(it.unit),
                false
            )
        }

        arguments?.getLong(ARG_SCHEDULE_ID)?.let { mScheduleId = it }

        unitString = UNIT.map { SkripsiConstant.getScheduleReminderUnitString(it) }

        requireContext().let { context ->
            val unitAdapter: ArrayAdapter<String> =
                ArrayAdapter<String>(context, R.layout.item_dropdown, unitString)
            mBinding.dropdownUnit.setAdapter(unitAdapter)
        }

        mBinding.buttonClose.setOnClickListener { dismiss() }
        mBinding.buttonAdd.setOnClickListener {
            val amount = mBinding.edittextAmount.text.toString().trim()

            if (amount.isEmpty()) {
                mBinding.inputlayoutAmount.error = "Harus diisi"
            } else {
                val selectedUnit = when (mBinding.dropdownUnit.text.toString()) {
                    unitString[0] -> SCHEDULE_REMINDER_UNIT_MINUTES
                    unitString[1] -> SCHEDULE_REMINDER_UNIT_HOURS
                    else -> SCHEDULE_REMINDER_UNIT_DAYS
                }

                mCallback?.onReminderAdded(
                    ReminderEntity(
                        mReminder?.id ?: 0,
                        amount.toInt(),
                        selectedUnit,
                        mBinding.switchPopup.isChecked,
                        mScheduleId,
                        ""
                    )
                )
                dismiss()
            }
        }

        return mBinding.root
    }

    fun setAddScheduleReminderDialogListener(callback: AddScheduleReminderDialogListener) {
        mCallback = callback
    }

    interface AddScheduleReminderDialogListener {
        fun onReminderAdded(reminder: ReminderEntity)
    }

    companion object {
        const val TAG = "ADD_SCHEDULE_REMINDER_DIALOG"
        const val ARG_REMINDER = "ARG_REMINDER"
        const val ARG_SCHEDULE_ID = "ARG_SCHEDULE_ID"
        val UNIT = mutableListOf(
            SCHEDULE_REMINDER_UNIT_MINUTES,
            SCHEDULE_REMINDER_UNIT_HOURS,
            SCHEDULE_REMINDER_UNIT_DAYS
        )
    }
}