package inas.anisha.skripsi_app.ui.main.schedule.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.ReminderEntity
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.databinding.FragmentAddScheduleBinding
import inas.anisha.skripsi_app.ui.common.RangeTimePickerDialog
import inas.anisha.skripsi_app.ui.common.TextValidator
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.isDateLaterThan
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.standardized
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toDateString
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toTimeString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.*


class AddScheduleDialog : DialogFragment() {

    private lateinit var mBinding: FragmentAddScheduleBinding
    private lateinit var mViewModel: ScheduleViewModel
    private lateinit var mRepository: Repository
    private lateinit var compositeDisposable: CompositeDisposable
    private var mCallback: AddScheduleDialogListener? = null
    private var mSchedule: ScheduleEntity? = null

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
        mSchedule?.let {
            mBinding.textviewTitle.text = "Edit Jadwal"
            mViewModel.fromEntity(it)
            initViewValues()
        }

        activity?.application?.let { mRepository = Repository.getInstance(it) }
        compositeDisposable = CompositeDisposable()

        mBinding.imageviewClose.setOnClickListener { dismiss() }
        mBinding.buttonSave.setOnClickListener { verifyData() }

        mBinding.imageviewRemove.setOnClickListener {
            mViewModel.executionTime = null
            mBinding.edittextExecutionTime.setText("")
            mBinding.textlayoutExecutionTime.error = null
            if (mBinding.edittextExecutionTime.hasFocus()) mBinding.edittextExecutionTime.clearFocus()
        }

        setChipGroupListener()
        setEditText()
        setRewardView()
        setReminderView()

        return mBinding.root
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }

    fun initViewValues() {
        when (mViewModel.type) {
            SkripsiConstant.SCHEDULE_TYPE_TASK -> {
                mBinding.chipTask.isChecked = true
                showHideTaskViews(true)
            }
            SkripsiConstant.SCHEDULE_TYPE_TEST -> {
                mBinding.chipTest.isChecked = true
                showHideTaskViews(false)
            }
            else -> {
                mBinding.chipActivity.isChecked = true
                showHideTaskViews(false)
            }
        }

        mBinding.edittextName.setText(mViewModel.name)
        mBinding.edittextDate.setText(mViewModel.endDate.toDateString())

        mBinding.edittextStartTime.setText(mViewModel.startDate.toTimeString())
        mBinding.edittexttEndTime.setText(mViewModel.endDate.toTimeString())
        mBinding.edittextDeadline.setText(mViewModel.endDate.toTimeString())

        mBinding.ediittextNote.setText(mViewModel.note)
        mBinding.rating.rating = mViewModel.priority.toFloat()

        mViewModel.reward.takeIf { it.isNotBlank() }?.let {
            mBinding.layoutRewards.layout.visibility = View.VISIBLE
            mBinding.textviewRewardsButton.text = "Edit"
            mBinding.layoutRewards.textviewTitle.text = it
        }

        mViewModel.executionTime?.let { mBinding.edittextExecutionTime.setText(it.toDateString()) }

        mViewModel.reminder?.let { initReminderData(it) }
    }

    fun setChipGroupListener() {
        mBinding.chipgroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.chip_task -> {
                    showHideTaskViews(true)
                    mViewModel.type = SkripsiConstant.SCHEDULE_TYPE_TASK
                }

                R.id.chip_test -> {
                    showHideTaskViews(false)
                    mViewModel.type = SkripsiConstant.SCHEDULE_TYPE_TEST
                }

                else -> {
                    showHideTaskViews(false)
                    mViewModel.type = SkripsiConstant.SCHEDULE_TYPE_ACTIVITY
                }
            }
        }
    }

    fun setEditText() {
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
                showTimePicker(mViewModel.startDate, ::setStartTime)
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showTimePicker(mViewModel.startDate, ::setStartTime)
            }
        }

        mBinding.edittexttEndTime.apply {
            setOnClickListener {
                showTimePicker(mViewModel.endDate, ::setEndTime)
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showTimePicker(mViewModel.endDate, ::setEndTime)
            }
        }

        mBinding.edittextDeadline.apply {
            setOnClickListener {
                showTimePicker(mViewModel.endDate, ::setEndTime)
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showTimePicker(mViewModel.endDate, ::setEndTime)
            }
        }

        mBinding.edittextName.addTextChangedListener(object :
            TextValidator(mBinding.edittextName) {
            override fun validate(textView: TextView, text: String) {
                mBinding.textlayoutName.error =
                    if (text.isEmpty()) "Nama jadwal harus diisi" else null
            }
        })

    }

    fun setRewardView() {
        mBinding.textviewRewardsButton.setOnClickListener { showAddRewardsDialog() }

        mBinding.layoutRewards.imageviewIcon.setImageResource(R.drawable.ic_trophy)
        mBinding.layoutRewards.textviewDescription.visibility = View.GONE
        mBinding.layoutRewards.textviewRemoveButton.setOnClickListener {
            mViewModel.reward = ""
            mBinding.layoutRewards.layout.visibility = View.GONE
            mBinding.textviewRewardsButton.text = "Tambahkan"
        }
    }

    fun setReminderView() {
        mBinding.textviewReminderButton.setOnClickListener { showAddReminderDialog() }

        mBinding.layoutReminder.imageviewIcon.setImageResource(R.drawable.ic_clock)
        mBinding.layoutReminder.textviewRemoveButton.setOnClickListener {
            mViewModel.setReminderValue(null)
            mBinding.layoutReminder.layout.visibility = View.GONE
            mBinding.textviewReminderButton.text = "Tambahkan"
        }
    }

    fun verifyData() {
        mViewModel.name = mBinding.edittextName.text.toString().trim()
        mViewModel.note = mBinding.ediittextNote.text.toString().trim()
        mViewModel.priority = mBinding.rating.rating.toInt()

        if (mViewModel.type == SkripsiConstant.SCHEDULE_TYPE_TASK) {
            mViewModel.startDate =
                Calendar.getInstance().apply { timeInMillis = mViewModel.endDate.timeInMillis }
        }

        mViewModel.setReminderValue(mViewModel.reminder)

        if (isValid(
                mViewModel.name,
                mViewModel.note,
                mViewModel.startDate,
                mViewModel.endDate,
                mViewModel.type
            )
        ) {
            if (mViewModel.type == SkripsiConstant.SCHEDULE_TYPE_ACTIVITY) {
                checkOverlappingSchedule()
            } else {
                saveSchedule()
            }
        }
    }

    fun isValid(
        name: String,
        note: String,
        startDate: Calendar,
        endDate: Calendar,
        type: Int
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            isValid = false
            mBinding.textlayoutName.error = "Nama jadwal harus diisi"
        } else {
            mBinding.textlayoutName.error = null
        }

        if (type == SkripsiConstant.SCHEDULE_TYPE_TASK) {
            if (mBinding.edittextDeadline.text.toString().isEmpty()) {
                isValid = false
                mBinding.textlayoutDeadline.error = "Waktu harus diisi"
            } else {
                mBinding.textlayoutDeadline.error = null
            }

            mViewModel.executionTime?.let {
                if (it > mViewModel.endDate) {
                    isValid = false
                    mBinding.textlayoutExecutionTime.error =
                        "Tanggal pengerjaan tidak boleh lebih dari waktu pengumpulan"
                } else {
                    mBinding.textlayoutExecutionTime.error = null
                }
            }

        } else {
            if (mBinding.edittextStartTime.text.toString().isEmpty()) {
                isValid = false
                mBinding.textlayoutStartTime.error = "Waktu harus diisi"
            } else {
                mBinding.textlayoutStartTime.error = null
            }

            if (mBinding.edittexttEndTime.text.toString().isEmpty()) {
                isValid = false
                mBinding.textlayoutEndTime.error = "Waktu harus diisi"
            } else {
                mBinding.textlayoutEndTime.error = null
            }

            if (mBinding.edittextStartTime.text.toString().isNotEmpty() &&
                mBinding.edittexttEndTime.text.toString().isNotEmpty() &&
                startDate >= endDate
            ) {
                isValid = false
                mBinding.textlayoutStartTime.error = "Waktu mulai harus lebih awal"
            }
        }

        if (mBinding.edittextDate.text.toString().isEmpty()) {
            isValid = false
            mBinding.textlayoutDueDate.error = "Tanggal harus diisi"
        } else {
            mBinding.textlayoutDueDate.error = null
        }

        if (name.length > 50 || note.length > 500) isValid = false

        return isValid
    }

    fun checkOverlappingSchedule() {
        mRepository.getOverlappingScheduleAndSchoolEntity(
            mViewModel.startDate,
            mViewModel.endDate,
            mSchedule?.id ?: -1,
            -1
        )
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.first.isNotEmpty() || it.second.isNotEmpty()) {
                    Snackbar.make(
                        mBinding.buttonSave,
                        "Kegiatan tidak boleh memiliki waktu yang sama dengan kegiatan atau jadwal sekolah lain",
                        Snackbar.LENGTH_LONG
                    )
                        .setAnchorView(mBinding.buttonSave).show()
                } else {
                    saveSchedule()
                }
            }.addTo(compositeDisposable)
    }

    fun saveSchedule() {
        mCallback?.onScheduleModified(mViewModel)
        dismiss()
    }

    fun showDatePicker(textView: TextView, isExecutionDate: Boolean) {
        val currentDate = (if (isExecutionDate) mViewModel.executionTime else mViewModel.endDate)
            ?: Calendar.getInstance().standardized()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        requireContext().let {
            val datePickerDialog = DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    if (isExecutionDate) {
                        mViewModel.executionTime =
                            currentDate.apply { set(year, monthOfYear, dayOfMonth) }.standardized()
                        mViewModel.executionTime?.let { executionDate ->
                            if (executionDate.isDateLaterThan(mViewModel.endDate)) {
                                mBinding.textlayoutExecutionTime.error =
                                    "Tanggal pengerjaan tidak boleh lebih dari waktu pengumpulan"
                            } else {
                                mBinding.textlayoutExecutionTime.error = null
                            }
                        }
                    } else {
                        mViewModel.startDate =
                            mViewModel.startDate.apply { set(year, monthOfYear, dayOfMonth) }
                                .standardized()
                        mViewModel.endDate =
                            mViewModel.endDate.apply { set(year, monthOfYear, dayOfMonth) }
                                .standardized()
                        mBinding.textlayoutDueDate.error = null
                    }
                    textView.text = currentDate.toDateString()
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }

    fun showTimePicker(defaultDate: Calendar, onTimeSet: (date: Calendar) -> Unit) {
        val date = Calendar.getInstance().apply { timeInMillis = defaultDate.timeInMillis }
        val hour = date[Calendar.HOUR_OF_DAY]
        val minutes = date[Calendar.MINUTE]

        requireContext().let {
            val timePicker = RangeTimePickerDialog(
                it,
                0,
                OnTimeSetListener { _, hour, minute ->
                    date.apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                    }.standardized()

                    onTimeSet(date)
                },
                hour,
                minutes,
                true
            )
            timePicker.show()
        }
    }

    fun setStartTime(date: Calendar) {
        if (mBinding.edittexttEndTime.text.toString().isNotEmpty() &&
            mViewModel.type != SkripsiConstant.SCHEDULE_TYPE_TASK && mViewModel.endDate <= date
        ) {
            mBinding.textlayoutStartTime.error = "Waktu mulai harus lebih awal"
            mBinding.textlayoutEndTime.error = null
        } else {
            mBinding.textlayoutStartTime.error = null
            mBinding.textlayoutEndTime.error = null
        }
        mViewModel.startDate = date.standardized()
        mBinding.edittextStartTime.setText(date.toTimeString())
    }

    fun setEndTime(date: Calendar) {
        if (mBinding.edittextStartTime.text.toString().isNotEmpty() &&
            mViewModel.type != SkripsiConstant.SCHEDULE_TYPE_TASK && date <= mViewModel.startDate
        ) {
            mBinding.textlayoutEndTime.error = "Waktu selesai harus lebih akhir"
            mBinding.textlayoutStartTime.error = null
            mBinding.textlayoutDeadline.error = null
        } else {
            mBinding.textlayoutEndTime.error = null
            mBinding.textlayoutStartTime.error = null
            mBinding.textlayoutDeadline.error = null
        }

        mViewModel.endDate = date.standardized()
        mBinding.edittexttEndTime.setText(date.toTimeString())
        mBinding.edittextDeadline.setText(date.toTimeString())
    }

    fun showHideTaskViews(isTask: Boolean) {
        if (isTask) {
            mBinding.groupTask.visibility = View.VISIBLE
            mBinding.groupNonTask.visibility = View.GONE
        } else {
            mBinding.groupTask.visibility = View.GONE
            mBinding.groupNonTask.visibility = View.VISIBLE
        }
    }

    fun showAddRewardsDialog() {
        val addRewardsDialog = AddScheduleRewardsDialog()
            .apply {
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
                mViewModel.reward = rewards
                mBinding.layoutRewards.layout.visibility = View.VISIBLE
                mBinding.layoutRewards.textviewTitle.text = rewards
                mBinding.textviewRewardsButton.text = "Edit"
            }
        })

        addRewardsDialog.show(
            childFragmentManager,
            AddScheduleRewardsDialog.TAG
        )
    }

    fun showAddReminderDialog() {
        val addReminderDialog = AddScheduleReminderDialog().apply {
            mViewModel.reminder?.let {
                arguments = Bundle().apply {
                    putParcelable(AddScheduleReminderDialog.ARG_REMINDER, it)
                }
            }
        }

        addReminderDialog.setAddScheduleReminderDialogListener(object :
            AddScheduleReminderDialog.AddScheduleReminderDialogListener {
            override fun onReminderAdded(reminder: ReminderEntity) {
                mViewModel.setReminderValue(reminder)
                initReminderData(reminder)
            }
        })

        addReminderDialog.show(
            childFragmentManager,
            AddScheduleReminderDialog.TAG
        )
    }

    fun initReminderData(reminder: ReminderEntity) {
        mBinding.layoutReminder.layout.visibility = View.VISIBLE
        mBinding.layoutReminder.textviewTitle.text =
            "${reminder.amount} ${SkripsiConstant.getScheduleReminderUnitString(reminder.unit)}"
        mBinding.layoutReminder.textviewDescription.text =
            if (reminder.isPopup) "Tampilkan pop up" else ""
        mBinding.layoutReminder.textviewDescription.visibility =
            if (reminder.isPopup) View.VISIBLE else View.GONE
        mBinding.textviewReminderButton.text = "Edit"
    }

    fun setAddScheduleDialogListener(callback: AddScheduleDialogListener) {
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