package inas.anisha.skripsi_app.ui.main.schedule.school

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.data.Repository
import inas.anisha.skripsi_app.data.db.entity.SchoolClassEntity
import inas.anisha.skripsi_app.databinding.FragmentAddSchoolClassBinding
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
        applyToViewModel()
        checkOverlappingSchedule()
    }

    fun applyToViewModel() {
        mViewModel.name = mBinding.edittextName.text.toString()
        mViewModel.note = mBinding.ediittextNote.text.toString()
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
                R.id.chip_monday -> mViewModel.day = Calendar.MONDAY
                R.id.chip_tuesday -> mViewModel.day = Calendar.TUESDAY
                R.id.chip_wednesday -> mViewModel.day = Calendar.WEDNESDAY
                R.id.chip_thursday -> mViewModel.day = Calendar.THURSDAY
                R.id.chip_friday -> mViewModel.day = Calendar.FRIDAY
                R.id.chip_saturday -> mViewModel.day = Calendar.SATURDAY
            }
        }
    }

    fun setEditText() {
        mBinding.edittextName.apply {
            imeOptions = EditorInfo.IME_ACTION_NEXT
            setRawInputType(InputType.TYPE_CLASS_TEXT)
        }

        mBinding.edittextStartTime.apply {
            setOnClickListener {
                showTimePicker(mViewModel.startTime) { date: Calendar ->
                    mViewModel.startTime = date
                    mBinding.edittextStartTime.setText(date.toTimeString())
                }
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showTimePicker(mViewModel.startTime) { date: Calendar ->
                    mViewModel.startTime = date
                    mBinding.edittextStartTime.setText(date.toTimeString())
                }
            }
        }

        mBinding.edittexttEndTime.apply {
            setOnClickListener {
                showTimePicker(mViewModel.endTime) { date: Calendar ->
                    mViewModel.endTime = date
                    mBinding.edittexttEndTime.setText(date.toTimeString())
                }
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) showTimePicker(mViewModel.endTime) { date: Calendar ->
                    mViewModel.endTime = date
                    mBinding.edittexttEndTime.setText(date.toTimeString())
                }
            }
        }
    }

    fun showTimePicker(defaultDate: Calendar, onTimeSet: (date: Calendar) -> Unit) {
        val date = Calendar.getInstance().apply { timeInMillis = defaultDate.timeInMillis }
        val hour = date[Calendar.HOUR_OF_DAY]
        val minutes = date[Calendar.MINUTE]

        requireContext().let {
            val timePicker = TimePickerDialog(
                it,
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