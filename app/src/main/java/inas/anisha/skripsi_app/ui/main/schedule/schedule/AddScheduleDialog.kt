package inas.anisha.skripsi_app.ui.main.schedule.schedule

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
import com.google.android.material.snackbar.Snackbar
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.constant.SkripsiConstant
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.databinding.FragmentAddScheduleBinding
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

        setChipGroupListener()
        setEditText()
        setRewardView()

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

        mBinding.imageviewRemove.setOnClickListener {
            mViewModel.executionTime = null
            mBinding.edittextExecutionTime.setText("")
            if (mBinding.edittextExecutionTime.hasFocus()) mBinding.edittextExecutionTime.clearFocus()
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

    }

    fun setRewardView() {
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
    }

    fun verifyData() {
        applyToViewModel()
        if (mViewModel.type == SkripsiConstant.SCHEDULE_TYPE_ACTIVITY) {
            checkOverlappingSchedule()
        } else {
            saveSchedule()
        }
    }

    fun applyToViewModel() {
        mViewModel.name = mBinding.edittextName.text.toString()
        mViewModel.note = mBinding.ediittextNote.text.toString()
        mViewModel.priority = mBinding.rating.rating.toInt()

        if (mViewModel.type == SkripsiConstant.SCHEDULE_TYPE_TASK) {
            mViewModel.startDate =
                Calendar.getInstance().apply { timeInMillis = mViewModel.endDate.timeInMillis }
        }
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
                    } else {
                        mViewModel.startDate =
                            currentDate.apply { set(year, monthOfYear, dayOfMonth) }.standardized()
                        mViewModel.endDate =
                            currentDate.apply { set(year, monthOfYear, dayOfMonth) }.standardized()
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
            val timePicker = TimePickerDialog(
                it,
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
        mViewModel.startDate = date.standardized()
        mBinding.edittextStartTime.setText(date.toTimeString())
    }

    fun setEndTime(date: Calendar) {
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
                if (rewards.isNotBlank()) {
                    mViewModel.reward = rewards
                    mBinding.layoutRewards.layout.visibility = View.VISIBLE
                    mBinding.layoutRewards.textviewTitle.text = rewards
                    mBinding.textviewRewardsButton.text = "Edit"
                }
            }
        })

        addRewardsDialog.show(
            childFragmentManager,
            AddScheduleRewardsDialog.TAG
        )
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