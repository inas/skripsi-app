package inas.anisha.srl_app.ui.main.schedule.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import inas.anisha.srl_app.R
import inas.anisha.srl_app.databinding.FragmentAddScheduleRewardsBinding

class AddScheduleRewardsDialog : DialogFragment() {
    private lateinit var mBinding: FragmentAddScheduleRewardsBinding
    private var mCallback: ConfirmationDialogListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_schedule_rewards,
                container,
                false
            )

        arguments?.getString(ARG_REWARDS)?.let { mBinding.edittextRewards.setText(it) }
        mBinding.buttonClose.setOnClickListener { dismiss() }
        mBinding.buttonConfirm.setOnClickListener {
            val reward = mBinding.edittextRewards.text.toString().trim()
            if (reward.isEmpty()) {
                mBinding.textlayoutRewards.error = "Penghargaan harus diisi"
            } else {
                mCallback?.onConfirmed(reward)
                dismiss()
            }
        }

        return mBinding.root
    }

    fun setConfirmationDialogListener(callback: ConfirmationDialogListener) {
        mCallback = callback
    }

    interface ConfirmationDialogListener {
        fun onConfirmed(rewards: String)
    }

    companion object {
        const val TAG = "ADD_SCHEDULE_REWARDS_DIALOG"
        const val ARG_REWARDS = "ARG_REWARDS"
    }
}