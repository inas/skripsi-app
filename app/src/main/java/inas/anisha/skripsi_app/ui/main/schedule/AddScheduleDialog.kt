package inas.anisha.skripsi_app.ui.main.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.databinding.FragmentAddScheduleBinding
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toTimeString
import java.util.*


class AddScheduleDialog : DialogFragment() {

    private lateinit var mBinding: FragmentAddScheduleBinding
    private lateinit var mViewModel: ScheduleViewModel
    private var mCallback: AddScheduleDialogListener? = null
    private var mSchedule: ScheduleEntity? = null

    private var startTime: Calendar? = null
    private var endTime: Calendar? = null
    private var deadlineTime: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
        mViewModel = ViewModelProviders.of(this).get(ScheduleViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_schedule,
                container,
                false
            )
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this
        mSchedule = arguments?.getParcelable(ARG_SCHEDULE)
        mSchedule?.let { mViewModel.fromEntity(it) }

        mBinding.imageviewClose.setOnClickListener { dismiss() }
        mBinding.imageviewRemove.setOnClickListener {
            mViewModel.executionTime = null
            mBinding.edittextExecutionTime.setText("")
            if (mBinding.edittextExecutionTime.hasFocus()) mBinding.edittextExecutionTime.clearFocus()
        }

        mBinding.textviewRewardsButton.setOnClickListener {
            showAddRewardsDialog()
        }

        mBinding.layoutRewards.imageviewIcon.setBackgroundDrawable(
            resources.getDrawable(
                R.drawable.ic_trophy,
                null
            )
        )
        mBinding.layoutRewards.textviewRemoveButton.setOnClickListener {
            mViewModel.reward = ""
            mBinding.layoutRewards.layout.visibility = View.GONE
            mBinding.textviewRewardsButton.text = "Tambahkan"
        }

        setChipGroupListener()
        setEditText()

        return mBinding.root
    }

    fun modifyTarget() {
//        val targetName = mBinding.edittextTarget.text.toString()
//        val targetNote = mBinding.edittextNote.text.toString()
//        val targetTime = mBinding.edittextTime.text.toString()
//
//        val target = TargetPendukungViewModel()
//            .apply {
//                id = mSchedule.id
//                name = targetName
//                note = targetNote
//                time = targetTime
//                isCompleted = mSchedule.isCompleted
//            }
//
//        mCallback?.let {
//            it.onTargetModified(target)
//            dismiss()
//        }

    }

    fun setChipGroupListener() {
        mBinding.chipgroup.setOnCheckedChangeListener { group, checkedId ->

            when (group.findViewById<Chip>(checkedId).text) {
                resources.getString(R.string.fragment_add_schedule_chip_task) -> {
                    showHideTime(false)
                    mViewModel.type = SkripsiConstant.SCHEDULE_TYPE_TASK
                    mBinding.groupNonActivity.visibility = View.VISIBLE
                    mBinding.groupNonTest.visibility = View.VISIBLE
                }

                resources.getString(R.string.fragment_add_schedule_chip_test) -> {
                    showHideTime(false)
                    mBinding.groupNonActivity.visibility = View.VISIBLE
                    mBinding.groupNonTest.visibility = View.GONE
                    mViewModel.type = SkripsiConstant.SCHEDULE_TYPE_TEST
                }

                else -> {
                    showHideTime(true)
                    mBinding.groupNonActivity.visibility = View.GONE
                    mViewModel.type = SkripsiConstant.SCHEDULE_TYPE_ACTIVITY
                }
            }
        }
    }

    fun setEditText() {
        mBinding.edittextName.apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            setRawInputType(InputType.TYPE_CLASS_TEXT)
        }

        mBinding.edittextDate.apply {
            setOnClickListener {
                showDatePicker(it as TextView, false)
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) showDatePicker(view as TextView, false)
            }
        }

        mBinding.edittextExecutionTime.apply {
            setOnClickListener {
                showDatePicker(it as TextView, true)
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) showDatePicker(view as TextView, true)
            }
        }

        mBinding.edittextStartTime.apply {
            setOnClickListener {
                showTimePicker(true)
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) showTimePicker(true)
            }
        }

        mBinding.edittexttEndTime.apply {
            setOnClickListener {
                showTimePicker(false)
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) showTimePicker(false)
            }
        }

        mBinding.edittextDeadline.apply {
            setOnClickListener {
                showTimePicker(false)
            }
            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) showTimePicker(false)
            }
        }

    }

    fun showDatePicker(textView: TextView, isExecutionDate: Boolean) {
        val currentDate = (if (isExecutionDate) mViewModel.executionTime else mViewModel.endDate)
            ?: Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        requireContext().let {
            val datePickerDialog = DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    if (isExecutionDate) {
                        mViewModel.executionTime =
                            currentDate.apply { set(year, monthOfYear, dayOfMonth) }
                    } else {
                        mViewModel.startDate =
                            currentDate.apply { set(year, monthOfYear, dayOfMonth) }
                        mViewModel.endDate =
                            currentDate.apply { set(year, monthOfYear, dayOfMonth) }
                    }
                    textView.text = dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    fun showTimePicker(isStartTime: Boolean) {
        val currentDate =
            if (isStartTime) mViewModel.startDate ?: mViewModel.endDate else mViewModel.endDate
        val hour = currentDate[Calendar.HOUR_OF_DAY]
        val minutes = currentDate[Calendar.MINUTE]

        requireContext().let {
            val timePicker = TimePickerDialog(
                it,
                OnTimeSetListener { _, hour, minute ->
                    currentDate.apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minutes)
                        set(Calendar.SECOND, 0)
                    }

                    if (isStartTime) {
                        mViewModel.startDate = currentDate
                        mBinding.edittextStartTime.setText(currentDate.toTimeString())
                    } else {
                        mViewModel.endDate = currentDate
                        mBinding.edittexttEndTime.setText(currentDate.toTimeString())
                        mBinding.edittextDeadline.setText(currentDate.toTimeString())
                    }
                },
                hour,
                minutes,
                true
            )
            timePicker.show()
        }

    }

    fun showHideTime(isActivity: Boolean) {
        if (isActivity) {
            mBinding.textlayoutDeadline.visibility = View.GONE
            mBinding.groupActivityTime.visibility = View.VISIBLE
        } else {
            mBinding.textlayoutDeadline.visibility = View.VISIBLE
            mBinding.groupActivityTime.visibility = View.GONE
        }
    }

    fun showAddRewardsDialog() {
        val addRewardsDialog = AddScheduleRewardsDialog().apply {
            arguments = Bundle().apply {
                putString(
                    AddScheduleRewardsDialog.ARG_REWARDS,
                    mViewModel.reward
                )
            }
        }

        addRewardsDialog.setConfirmationDialogListener(object :
            AddScheduleRewardsDialog.ConfirmationDialogListener {
            override fun onConfirmed(rewards: String) {
                if (rewards.isNotEmpty()) {
                    mViewModel.reward = rewards
                    mBinding.layoutRewards.layout.visibility = View.VISIBLE
                    mBinding.layoutRewards.textviewTitle.text = rewards
                    mBinding.textviewRewardsButton.text = "Edit"
                }
            }
        })

        addRewardsDialog.show(childFragmentManager, AddScheduleRewardsDialog.TAG)
    }

    fun setOnTargetAddedListener(callback: AddScheduleDialogListener) {
        mCallback = callback
    }

    interface AddScheduleDialogListener {
        fun onScheduleModified(schedule: ScheduleViewModel)
    }

    companion object {
        const val TAG = "SCHEDULE_EDIT_DIALOG"

        const val ARG_SCHEDULE = "ARG_SCHEDULE"
    }
}