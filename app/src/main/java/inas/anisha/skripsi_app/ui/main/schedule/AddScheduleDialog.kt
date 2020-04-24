package inas.anisha.skripsi_app.ui.main.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import inas.anisha.skripsi_app.R
import inas.anisha.skripsi_app.data.db.entity.ScheduleEntity
import inas.anisha.skripsi_app.databinding.FragmentAddScheduleBinding

class AddScheduleDialog : DialogFragment() {

    private lateinit var mBinding: FragmentAddScheduleBinding
    private lateinit var mViewModel: ScheduleViewModel
    private var mCallback: AddScheduleDialogListener? = null
    private var mSchedule: ScheduleEntity = ScheduleEntity(0)

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


        mBinding.imageviewClose.setOnClickListener { dismiss() }

        mSchedule = arguments?.getParcelable(ARG_SCHEDULE) ?: ScheduleEntity(0)

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