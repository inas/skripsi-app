package inas.anisha.skripsi_app.ui.main.schedule.school

import android.app.TimePickerDialog
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
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.SchoolClassEntity
import inas.anisha.skripsi_app.databinding.FragmentAddSchoolClassBinding
import inas.anisha.skripsi_app.ui.common.TextValidator
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.isTimeEarlierThan
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.standardized
import inas.anisha.skripsi_app.utils.CalendarUtil.Companion.toTimeString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.*

class AddSchoolClassDialog : DialogFragment() {

    private lateinit var mBinding: FragmentAddSchoolClassBinding
    private lateinit var mViewModel: SchoolClassViewModel
    private lateinit var mRepository: Repository
    private lateinit var compositeDisposable: CompositeDisposable
    private var mCallback: AddSchoolDialogCallback? = null
    private var mClass: SchoolClassEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
        mViewModel = ViewModelProviders.of(this).get(SchoolClassViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_school_class,
                container,
                false
            )
        mBinding.viewModel = mViewModel
        mBinding.lifecycleOwner = this
        mClass = arguments?.getParcelable(ARG_CLASS)
        mClass?.let {
            mBinding.textviewTitle.text = "Edit Jadwal Sekolah"
            mViewModel.fromEntity(it)
            mBinding.viewModel = mViewModel

            mBinding.edittextStartTime.setText(mViewModel.startTimeText())
            mBinding.edittexttEndTime.setText(mViewModel.endTimeText())

            initChipValue()
        }

        activity?.application?.let { mRepository = Repository.getInstance(it) }
        compositeDisposable = CompositeDisposable()

        mBinding.imageviewClose.setOnClickListener { dismiss() }
        mBinding.buttonSave.setOnClickListener { verifyData() }

        setChipGroupListener()
        setEditText()

        return mBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
    }

    fun initChipValue() {
        when (mViewModel.day) {
            Calendar.MONDAY -> mBinding.chipMonday.isChecked = true
            Calendar.TUESDAY -> mBinding.chipTuesday.isChecked = true
            Calendar.WEDNESDAY -> mBinding.chipWednesday.isChecked = true
            Calendar.THURSDAY -> mBinding.chipThursday.isChecked = true
            Calendar.FRIDAY -> mBinding.chipFriday.isChecked = true
            Calendar.SATURDAY -> mBinding.chipSaturday.isChecked = true
        }
    }

    fun verifyData() {
        mViewModel.name = mBinding.edittextName.text.toString().trim()
        mViewModel.note = mBinding.ediittextNote.text.toString().trim()

        if (isValid(mViewModel.name, mViewModel.note, mViewModel.startTime, mViewModel.endTime))
            checkOverlappingSchedule()
    }

    fun isValid(name: String, note: String, startTime: Calendar, endTime: Calendar): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            isValid = false
            mBinding.textlayoutName.error = "Nama jadwal harus diisi"
        } else {
            mBinding.textlayoutName.error = null
        }

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
            !startTime.isTimeEarlierThan(endTime)
        ) {
            isValid = false
            mBinding.textlayoutStartTime.error = "Waktu mulai harus lebih awal"
        }

        if (name.length > 50 || note.length > 500) isValid = false

        return isValid
    }

    fun checkOverlappingSchedule() {
        mRepository.getOverlappingClass(mViewModel.startTime, mViewModel.endTime, mClass?.id ?: -1)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNotEmpty()) {
                    Snackbar.make(
                        mBinding.buttonSave,
                        "Jadwal sekolah tidak boleh memiliki waktu yang sama dengan jadwal sekolah lain",
                        Snackbar.LENGTH_LONG
                    )
                        .setAnchorView(mBinding.buttonSave).show()
                } else {
                    saveSchoolClass()
                }
            }.addTo(compositeDisposable)
    }

    fun saveSchoolClass() {
        mCallback?.onSubmit(mViewModel)
        dismiss()
    }

    fun setChipGroupListener() {
        mBinding.chipgroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_monday -> setDay(Calendar.MONDAY)
                R.id.chip_tuesday -> setDay(Calendar.TUESDAY)
                R.id.chip_wednesday -> setDay(Calendar.WEDNESDAY)
                R.id.chip_thursday -> setDay(Calendar.THURSDAY)
                R.id.chip_friday -> setDay(Calendar.FRIDAY)
                R.id.chip_saturday -> setDay(Calendar.SATURDAY)
            }
        }
    }

    private fun setDay(day: Int) {
        mViewModel.day = day
        mViewModel.startTime.set(Calendar.DAY_OF_WEEK, day)
        mViewModel.endTime.set(Calendar.DAY_OF_WEEK, day)
    }

    fun setEditText() {
        mBinding.edittextStartTime.apply {
            setOnClickListener {
                showTimePicker(mViewModel.startTime, ::setStartTime)
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showTimePicker(mViewModel.startTime, ::setStartTime)
            }
        }

        mBinding.edittexttEndTime.apply {
            setOnClickListener {
                showTimePicker(mViewModel.endTime, ::setEndTime)
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showTimePicker(mViewModel.endTime, ::setEndTime)
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

    fun setStartTime(date: Calendar) {
        if (mBinding.edittexttEndTime.text.toString().isNotEmpty()
            && !date.isTimeEarlierThan(mViewModel.endTime)
        ) {
            mBinding.textlayoutStartTime.error = "Waktu mulai harus lebih awal"
            mBinding.textlayoutEndTime.error = null
        } else {
            mBinding.textlayoutStartTime.error = null
            mBinding.textlayoutEndTime.error = null
        }

        mViewModel.startTime = date.standardized()
        mBinding.edittextStartTime.setText(date.toTimeString())
    }

    fun setEndTime(date: Calendar) {
        if (mBinding.edittextStartTime.text.toString().isNotEmpty()
            && !mViewModel.startTime.isTimeEarlierThan(date)
        ) {
            mBinding.textlayoutEndTime.error = "Waktu selesai harus lebih akhir"
            mBinding.textlayoutStartTime.error = null
        } else {
            mBinding.textlayoutStartTime.error = null
            mBinding.textlayoutEndTime.error = null
        }

        mViewModel.endTime = date.standardized()
        mBinding.edittexttEndTime.setText(date.toTimeString())
    }

    fun showTimePicker(defaultDate: Calendar, onTimeSet: (date: Calendar) -> Unit) {
        val date = Calendar.getInstance().apply { timeInMillis = defaultDate.timeInMillis }
        val hour = date[Calendar.HOUR_OF_DAY]
        val minutes = date[Calendar.MINUTE]

        requireContext().let {
            val timePicker = TimePickerDialog(
                it,
                0,
                TimePickerDialog.OnTimeSetListener { _, hour, minute ->
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

    fun setAddSchoolDialogCallback(callback: AddSchoolDialogCallback) {
        mCallback = callback
    }

    interface AddSchoolDialogCallback {
        fun onSubmit(viewModel: SchoolClassViewModel)
    }

    companion object {
        const val TAG = "ADD_SCHOOL_CLASS_DIALOG"
        const val ARG_CLASS = "ARG_CLASS"
    }
}